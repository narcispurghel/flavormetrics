package com.flavormetrics.api.exception;

public class RecipeNotFoundException extends RuntimeException {

    public RecipeNotFoundException() {
        super("Recipe not found");
    }

}
