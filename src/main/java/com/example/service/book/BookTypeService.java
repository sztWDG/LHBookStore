package com.example.service.book;

import com.example.repository.book.BookTypeRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookTypeService {
    @Resource
    private BookTypeRepository bookTypeRepository;
}
