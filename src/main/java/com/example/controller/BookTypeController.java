package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.response.book.BookTypeResp;
import com.example.service.book.BookTypeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-type")
public class BookTypeController {
    @Resource
    private BookTypeService bookTypeService;

    @Operation(summary = "展示所有书本类型")
    @GetMapping
    public RestBean<List<BookTypeResp>> getBookType() {
        return RestBean.success(bookTypeService.getAll());
    }

    @Operation(summary = "新增书本类型")
    @PostMapping
    public RestBean<Void> addBookType(@RequestParam @NotBlank String name){

        bookTypeService.addBookType(name);

        return RestBean.success();
    }

    @Operation(summary = "更新书本类型")
    @PutMapping("/{id}")
    public RestBean<Void> updateBookType(@PathVariable Long id, @RequestParam @NotBlank String name){
        bookTypeService.updateBookType(id, name);
        return RestBean.success();
    }

    @Operation(summary = "删除书本类型")
    @DeleteMapping("/{id}")
    public RestBean<Void> deleteBookType(@PathVariable Long id){

        bookTypeService.deleteBookTypeById(id);
        return RestBean.success();
    }
}
