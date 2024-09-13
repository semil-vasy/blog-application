package com.example.blog.dto.blog;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class BlogDTO implements Serializable {
    private String blogId;

    @NotEmpty(message = "Please insert title")
    @Size(min = 3, message = "Title must be min of 3 characters !!")
    private String title;

    @NotEmpty
    private String banner;

    @NotEmpty(message = "Please enter some content")
    private String content;

    @NotEmpty
    @Size(min = 3, max = 200, message = "Description must be min of 3 and max of 200 characters !!")
    private String description;

    private boolean draft;

    private Date createdAt;

    @NotEmpty
    @JsonDeserialize(using = TagDTOSetDeserializer.class)
    private Set<TagDTO> tags;
}

class TagDTOSetDeserializer extends JsonDeserializer<Set<TagDTO>> {
    @Override
    public Set<TagDTO> deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        List<String> tagNames = jsonParser.readValueAs(new TypeReference<List<String>>() {
        });
        return tagNames.stream().map(TagDTO::new).collect(Collectors.toSet());
    }
}
