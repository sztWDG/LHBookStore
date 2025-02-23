package com.example.entity.vo.response.book;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookTypeResp {
    Long id;
    String name;
}
