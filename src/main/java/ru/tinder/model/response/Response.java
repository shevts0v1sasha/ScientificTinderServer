package ru.tinder.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {

    private T data;
    private ResponseStatus responseStatus;
    private String description;

}
