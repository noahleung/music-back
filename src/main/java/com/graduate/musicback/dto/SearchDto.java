package com.graduate.musicback.dto;

import lombok.Data;

@Data
public class SearchDto {
    private int page;
    private int size;
    private String keywords;
}
