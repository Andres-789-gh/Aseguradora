package com.exe.app.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClientDto {
    private Long id;
    private String doc;
    private String name;
    private String email;
}
