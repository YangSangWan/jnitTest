package site.metacoding.junitproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import site.metacoding.junitproject.domain.Book;
import site.metacoding.junitproject.domain.BookRepository;
import site.metacoding.junitproject.util.MailSender;
import site.metacoding.junitproject.util.MailSenderStub;
import site.metacoding.junitproject.web.dto.request.BookSaveReqDto;
import site.metacoding.junitproject.web.dto.response.BookRespDto;

@ExtendWith(MockitoExtension.class) //가짜 메모리 환경
public class BookServiceTest {
    

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MailSender mailsender;

    @Test
    public void 책등록하기_테스트(){

        //given
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("junit강의");
        dto.setAuthor("메타코딩");

        //stub (가설)
        when(bookRepository.save(any())).thenReturn(dto.toEntity());
        when(mailsender.send()).thenReturn(true);
        

        //when
        BookRespDto bookRespDto = bookService.책등록하기(dto);

        //then
        assertEquals(dto.getTitle(), bookRespDto.getTitle());
        assertEquals(dto.getAuthor(), bookRespDto.getAuthor());
    }

    @Test
    public void 책목록보기_테스트(){
        //given

        //stub
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "junit강의", "메타코딩"));
        books.add(new Book(2L, "spring강의", "겟인데어"));

        when(bookRepository.findAll()).thenReturn(books);

        //when
        List<BookRespDto> dtos = bookService.책목록보기();

        //then
        assertEquals(dtos.get(0).getTitle(), "junit강의");
        System.out.println(dtos.get(0).getTitle());
        System.out.println(dtos.get(1).getTitle());
    }

    @Test
    public void 책한번보기_테스트(){
        // given
        Long id = 1L;

        // stub
        Book book = new Book(1L, "junit강의", "메타코딩");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        // when 
        BookRespDto bookRespDto = bookService.책한건보기(id);

        // then
        assertEquals(bookRespDto.getTitle(), book.getTitle());
    }

    @Test
    public void 책수정하기_테스트(){
        // given
        Long id = 1L;
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("spring 강의"); 
        dto.setAuthor("겟인데어");

        // stub
        Book book = new Book(1L, "junit강의", "메타코딩");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        // when
        BookRespDto bookRespDto = bookService.책수정하기(id, dto);

        // then
        assertEquals(bookRespDto.getTitle(), dto.getTitle());
    }
}
