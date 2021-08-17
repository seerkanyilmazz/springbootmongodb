package com.seerkanyilmazz.springbootmongodb.service;

import com.seerkanyilmazz.springbootmongodb.exception.TodoCollectionException;
import com.seerkanyilmazz.springbootmongodb.model.TodoDTO;

import javax.validation.ConstraintViolationException;

public interface ITodoService {

    public void createTodo(TodoDTO todo) throws ConstraintViolationException, TodoCollectionException;
}
