package com.edig.searchengine.exceptions;

public class CategoryNotExistsException extends RuntimeException{
    public CategoryNotExistsException(String message) {
        super(message);
    }
}
