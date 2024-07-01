package com.hallym.booker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class Result<T> {
    private T data;

}
