package com.example.service.book;

import com.example.entity.dto.BookType;
import com.example.entity.vo.response.book.BookTypeResp;
import com.example.exception.book.BookTypeBindBookIsNotNullException;
import com.example.exception.book.BookTypeNameIsExistsException;
import com.example.repository.book.BookRepository;
import com.example.repository.book.BookTypeRepository;
import io.github.linpeilie.Converter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BookTypeService {
    @Resource
    private BookTypeRepository bookTypeRepository;
    @Resource
    private Converter converter;
    @Resource
    private BookRepository bookRepository;

    public List<BookTypeResp> getAll() {
        return bookTypeRepository
                .list()
                .stream()
                .map(item -> converter.convert(item, BookTypeResp.class)).toList();
    }

    public void addBookType(String name) {
        checkBookTypeIsExists(name);
        BookType bookType = new BookType();
        bookType.setName(name);
        bookTypeRepository.save(bookType);
    }


    public void updateBookType(Long id, String name) {
        checkBookTypeIsExists(name);
        bookTypeRepository.lambdaUpdate()
                .eq(BookType::getId, id)
                .set(BookType::getName, name);
    }

    private void checkBookTypeIsExists(String name) {
        if (bookTypeRepository.isNameExist(name)) {
            throw new BookTypeNameIsExistsException();
        }
    }

    public void deleteBookTypeById(Long id) {
        //要删除之前，要校验一下，若该类型下还有存在书本，则拒绝删除
        //要引入BookRepository以查询是否有该类型书本
        //1.通过typeId查找出该类型所有图书，若List为空则说明无 ✔
        //2.直接通过typeId查找数据库中是否存在该图书
        if (!bookRepository.getBooksByTypeId(id)){
            throw new BookTypeBindBookIsNotNullException();
        }
        bookTypeRepository.removeById(id);
    }
}
