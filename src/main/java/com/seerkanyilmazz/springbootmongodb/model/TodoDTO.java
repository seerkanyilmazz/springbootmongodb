package com.seerkanyilmazz.springbootmongodb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Setter //Setter method Lombok.
@Getter //Getter with Lombok.
@AllArgsConstructor //@AllArgsConstructor generates a constructor requiring an argument for every field in the annotated class.
@NoArgsConstructor //@NoArgsConstructor generates a default constructor with no parameters.
@Document(collection = "todos") // Its for non relational database.
public class TodoDTO {

    @Id //Primary key
    private String id;

    private String todo;

    private String description;

    private Boolean status;

    private Date createdAt;

    private Date updatedAt;
}
