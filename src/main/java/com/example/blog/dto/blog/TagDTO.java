package com.example.blog.dto.blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO implements Serializable {
    private Long id;
    private String name;

    public TagDTO(String name) {
        this.name = name;
    }
}
