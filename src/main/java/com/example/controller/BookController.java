package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.entity.RestBean;
import com.example.entity.vo.request.BookSaveReq;
import com.example.entity.vo.response.BooksResp;
import com.example.service.BookService;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book")
public class BookController {
    @Resource
    private BookService bookService;

    //查找、展示书本
    @GetMapping("/books")
    public RestBean<IPage<BooksResp>> showBooks(
            @RequestParam int pageIndex,
            @RequestParam int pageSize,
            @RequestParam String name,
            @RequestParam String author,
            @RequestParam @Nullable Long typeId
    ) {
        return RestBean.success(bookService.showBooks(pageIndex, pageSize, name, author, typeId));

    }

    //添加书本
    @PostMapping
    public RestBean<Void> addBook(@RequestBody @Valid BookSaveReq req) {
        bookService.addBook(req);
        return RestBean.success();
    }

    //更新书本
    @PutMapping("/{id}")
    public RestBean<Void> updateBook(
            @PathVariable Long id,
            @RequestBody @Valid BookSaveReq req) {
        bookService.updateBook(id, req);
        return RestBean.success();
    }

    //删除书本
    @DeleteMapping("/{id}")
    public RestBean<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return RestBean.success();
    }


}
