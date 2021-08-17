package com.seerkanyilmazz.springbootmongodb.service;

import com.seerkanyilmazz.springbootmongodb.Repository.ITodoRepository;
import com.seerkanyilmazz.springbootmongodb.exception.TodoCollectionException;
import com.seerkanyilmazz.springbootmongodb.model.TodoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements ITodoService {

    @Autowired
    private ITodoRepository iTodoRepository;

    @Override
    public void createTodo(TodoDTO todo) throws ConstraintViolationException, TodoCollectionException {
        Optional<TodoDTO> optionalTodoDTO = iTodoRepository.findByTodo(todo.getTodo());

        if (optionalTodoDTO.isPresent()){
            throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExists());
        }else {
            int sequence = getTodos();

            if (sequence == 0){
                todo.setCreatedAt(new Date(System.currentTimeMillis()));
                todo.setId("1");
            }else{
                sequence++;
                String sequenceString = String.valueOf(sequence);
                todo.setCreatedAt(new Date(System.currentTimeMillis()));
                todo.setId(sequenceString);
                iTodoRepository.save(todo);
            }

            todo.setCreatedAt(new Date(System.currentTimeMillis()));
            iTodoRepository.save(todo);
        }
    }

    private int getTodos() {
        List<TodoDTO> todos = iTodoRepository.findAll();
        return todos.size();
    }
}
