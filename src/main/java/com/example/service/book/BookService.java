package com.example.service.book;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.dto.Book;
import com.example.entity.enums.BookStatusEnum;
import com.example.entity.vo.request.book.BookSaveReq;
import com.example.entity.vo.response.book.BooksResp;
import com.example.exception.book.BookIsbnIsExistsException;
import com.example.exception.book.BookIsbnIsNotAllowModifyException;
import com.example.exception.book.BookNumberIsNotEnoughException;
import com.example.repository.book.BookRepository;
import io.github.linpeilie.Converter;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class BookService {
    @Resource
    private BookRepository bookRepository;
    @Resource
    private Converter converter;


    //Qxk：分页
    public IPage<BooksResp> showBooks(
            int pageIndex,
            int pageSize,
            @Nullable String name,
            @Nullable String author,
            @Nullable Long typeId
    ) {
        Page<Book> page = Page.of(pageIndex, pageSize);
        //QxkTips:使用Lambda代替
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        //.or
//        if (StringUtils.isNotBlank(name)) {
//            queryWrapper.like(StringUtils.isNotBlank(name),Book::getName, name);
//        }
//
//        if (StringUtils.isNotBlank(author)) {
//            queryWrapper.like(Book::getAuthor, author);
//        }
//        queryWrapper.like(Book::getName, name)
//                .like(Book::getAuthor, author);
//        if (typeId != null) {
//            queryWrapper.eq(Book::getTypeId, typeId);
//        }

        queryWrapper.like(StringUtils.isNotBlank(name),Book::getName, name)
                .like(StringUtils.isNotBlank(author),Book::getAuthor, author)
                .eq(Objects.nonNull(typeId),Book::getTypeId, typeId);

        //QxkTips1过滤掉已经下架的
//        IPage<BooksResp> temp = bookRepository.page(
//                page,
//                queryWrapper
//        ).convert(book -> converter.convert(book, BooksResp.class));
//        temp.setRecords(temp.getRecords().stream().filter(item ->
//                item.getStatus() != BookStatusEnum.De_List
//        ).toList());
        return bookRepository.page(
                page,
                queryWrapper
        ).convert(book -> converter.convert(book, BooksResp.class));
    }

    public void addBook(BookSaveReq req) {

        if (bookRepository.isBookIsbnExists(req.getIsbn())) {
            throw new BookIsbnIsExistsException();
        }
        Book book = converter.convert(req, Book.class);
        bookRepository.save(book);
    }

    public void updateBook(Long id, BookSaveReq req) {
        Book book = bookRepository.getById(id);
        if (!Objects.equals(book.getIsbn(), req.getIsbn())) {
            throw new BookIsbnIsNotAllowModifyException();
        }
        Book modifyBook = converter.convert(req, Book.class);
        //QxkQuestion:设置ID
        modifyBook.setId(id);
        bookRepository.updateById(modifyBook);
    }

    public void deleteBook(Long id) {
        bookRepository.removeById(id);
    }


    //更新图书状态
    public void updateStatus(Long id, BookStatusEnum status) {

        Book book = bookRepository.getById(id);

        if (status == BookStatusEnum.En_List) {
            if (book.getNumber() <= 0){
                throw new BookNumberIsNotEnoughException();
            }
        }

        bookRepository.lambdaUpdate().eq(Book::getId, id).set(Book::getStatus, status);
    }
}
