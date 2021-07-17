package ru.gb_dz2.dto;

import lombok.*;

@Data
public class ErrorBody {
    private Integer status;
    private String message;
    private String timestamp;
    private String path;
    private String error;

}
