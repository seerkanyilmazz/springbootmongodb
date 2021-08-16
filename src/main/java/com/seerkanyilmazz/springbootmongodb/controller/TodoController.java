package com.seerkanyilmazz.springbootmongodb.controller;

import com.seerkanyilmazz.springbootmongodb.Repository.TodoRepository;
import com.seerkanyilmazz.springbootmongodb.model.TodoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController //is a convenience annotation for creating Restful controllers.
public class TodoController {

    @Autowired 
    public TodoRepository todoRepository;

    //Get all todos
    @GetMapping("/todos")
    public ResponseEntity<?> getAllTodos(){
        List<TodoDTO> todos = todoRepository.findAll();

        if (todos.size() > 0 ){
            return new ResponseEntity<List<TodoDTO>>(todos, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("There is no available todos.", HttpStatus.NOT_FOUND);
        }
    }

    //Create Todo
    @PostMapping("/todos")
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todo){
        try {
            List<TodoDTO> todos = todoRepository.findAll();

            if (todos.isEmpty() == true){
                todo.setCreatedAt(new Date(System.currentTimeMillis()));
                todo.setId("1");
            }else{
                TodoDTO lastItemOfTodos = todos.get(todos.size()-1);
                String sequence = lastItemOfTodos.getId();
                int sequenceInt = Integer.parseInt(sequence);

                todo.setCreatedAt(new Date(System.currentTimeMillis()));
                sequenceInt++;
                todo.setId(String.valueOf(sequenceInt));
                todoRepository.save(todo);
            }

            todo.setCreatedAt(new Date(System.currentTimeMillis()));
            todoRepository.save(todo);

            return new ResponseEntity<TodoDTO>(todo, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get todo via id
    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getSingleTodo(@PathVariable("id") String id){
        Optional<TodoDTO> todoDTOOptional = todoRepository.findById(id);

        if (todoDTOOptional.isPresent()){
            return new ResponseEntity<>(todoDTOOptional.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Todo not found with id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    //Update todo via id
    @PutMapping("/todos/{id}")
    public ResponseEntity<?> updateById(@PathVariable("id") String id, @RequestBody TodoDTO todoDTO){
        Optional<TodoDTO> todoDTOOptional = todoRepository.findById(id);

        if (todoDTOOptional.isPresent()){
            TodoDTO todoToSave = todoDTOOptional.get();
            todoToSave.setStatus(todoDTO.getStatus() != null ? todoDTO.getStatus() : todoToSave.getStatus());
            todoToSave.setTodo(todoDTO.getTodo() != null ? todoDTO.getTodo() : todoToSave.getTodo());
            todoToSave.setDescription(todoDTO.getDescription() != null ? todoDTO.getDescription() : todoToSave.getDescription());
            todoToSave.setUpdatedAt(new Date(System.currentTimeMillis()));

            todoRepository.save(todoToSave);

            return new ResponseEntity<>(todoToSave, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Todo not found with id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    //delete todo via id
    @DeleteMapping("/todos/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") String id){
        try {
            todoRepository.deleteById(id);
            return new ResponseEntity<>("Deleting Successful by id" + id, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
