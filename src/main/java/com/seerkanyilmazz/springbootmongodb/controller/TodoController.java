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

    @GetMapping("/todos")
    public ResponseEntity<?> getAllTodos(){
        List<TodoDTO> todos = todoRepository.findAll();

        if (todos.size() > 0 ){
            return new ResponseEntity<List<TodoDTO>>(todos, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("There is no available todos.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/todos")
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todo){
        try {
            todo.setCreatedAt(new Date(System.currentTimeMillis()));
            todoRepository.save(todo);
            return new ResponseEntity<TodoDTO>(todo, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getSingleTodo(@PathVariable("id") String id){
        Optional<TodoDTO> todoDTOOptional = todoRepository.findById(id);

        if (todoDTOOptional.isPresent()){
            return new ResponseEntity<>(todoDTOOptional.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Todo not found with id: " + id, HttpStatus.NOT_FOUND);
        }
    }

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
