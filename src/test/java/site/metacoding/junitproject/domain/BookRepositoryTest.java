package site.metacoding.junitproject.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("dev") //dev모드일때만 작동
@DataJpaTest //DB와 관련된 컴토넌트만 메모리에 로딩
public class BookRepositoryTest {
    
    @Autowired // DI
    private BookRepository bookRepository;

    //@BeforeAll //테스트 시작전에 한번만 실행
    @BeforeEach // 각 테스트 시작전에 한번씩 실행
    public void 데이터준비(){
        String title = "junit";
        String author = "겟인데어";
        Book book = Book.builder()
                    .title(title)
                    .author(author)
                    .build();
        bookRepository.save(book);
    }

    //1. 책등록
    @Test
    public void 책등록_test(){
        // given(데이터 준비)
        String title = "junit5";
        String author = "메타코딩";
        Book book = Book.builder()
                    .title(title)
                    .author(author)
                    .build();

        // when(테스트 실행)
        Book bookPs = bookRepository.save(book);

        // then(검증)
        assertEquals(title, bookPs.getTitle());
        assertEquals(author, bookPs.getAuthor());
    }

    //2. 책 목록보기
    @Test
    public void 책목록보기_test(){

        // given(데이터 준비)
        String title = "junit";
        String author = "겟인데어";

        List<Book> booksPS = bookRepository.findAll();

        assertEquals(title, booksPS.get(0).getTitle());
        assertEquals(author, booksPS.get(0).getAuthor());
    }

    //3. 책 한건 보기
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책한건보기_test(){
        // given(데이터 준비)
        String title = "junit";
        String author = "겟인데어";
        
                    
        // when(테스트 실행)
        Book bookPS = bookRepository.findById(1L).get();
        
        //then
        assertEquals(title, bookPS.getTitle());


    }

    //4. 책 삭제
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책삭제_test(){
        Long id = 1L;
        bookRepository.deleteById(id);
        Optional<Book> bookPS = bookRepository.findById(id);

        assertFalse(bookPS.isPresent());
    }

    //5. 책 수정
}
