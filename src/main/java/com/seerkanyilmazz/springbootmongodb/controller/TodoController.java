package com.seerkanyilmazz.springbootmongodb.controller;

import com.seerkanyilmazz.springbootmongodb.Repository.ITodoRepository;
import com.seerkanyilmazz.springbootmongodb.exception.TodoCollectionException;
import com.seerkanyilmazz.springbootmongodb.model.TodoDTO;
import com.seerkanyilmazz.springbootmongodb.service.ITodoService;
import com.seerkanyilmazz.springbootmongodb.service.TodoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController //is a convenience annotation for creating Restful controllers.
public class TodoController {

    @Autowired 
    private ITodoRepository iTodoRepository;

    @Autowired
    private ITodoService iTodoService;

    //Get all todos
    @GetMapping("/todos")
    public ResponseEntity<?> getAllTodos(){
        List<TodoDTO> todos = iTodoRepository.findAll();

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
            iTodoService.createTodo(todo);
            return new ResponseEntity<TodoDTO>(todo, HttpStatus.OK);
        }catch (ConstraintViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (TodoCollectionException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    //Get todo via id
    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getSingleTodo(@PathVariable("id") String id){
        Optional<TodoDTO> todoDTOOptional = iTodoRepository.findById(id);

        if (todoDTOOptional.isPresent()){
            return new ResponseEntity<>(todoDTOOptional.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Todo not found with id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    //Update todo via id
    @PutMapping("/todos/{id}")
    public ResponseEntity<?> updateById(@PathVariable("id") String id, @RequestBody TodoDTO todoDTO){
        Optional<TodoDTO> todoDTOOptional = iTodoRepository.findById(id);

        if (todoDTOOptional.isPresent()){
            TodoDTO todoToSave = todoDTOOptional.get();
            todoToSave.setStatus(todoDTO.getStatus() != null ? todoDTO.getStatus() : todoToSave.getStatus());
            todoToSave.setTodo(todoDTO.getTodo() != null ? todoDTO.getTodo() : todoToSave.getTodo());
            todoToSave.setDescription(todoDTO.getDescription() != null ? todoDTO.getDescription() : todoToSave.getDescription());
            todoToSave.setUpdatedAt(new Date(System.currentTimeMillis()));

            iTodoRepository.save(todoToSave);

            return new ResponseEntity<>(todoToSave, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Todo not found with id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    //delete todo via id
    @DeleteMapping("/todos/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") String id){
        try {
            iTodoRepository.deleteById(id);
            return new ResponseEntity<>("Deleting Successful by id " + id, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
