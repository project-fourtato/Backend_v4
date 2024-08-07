
package com.hallym.booker.dto;

import java.util.List;

public class Result<T> {
    private List<T> data;
    private String message;

    public Result(List<T> data) {
        this.data = data;
        this.message = "";
    }

    public Result(List<T> data, String message) {
        this.data = data;
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}