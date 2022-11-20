package site.metacoding.junitproject.web;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import site.metacoding.junitproject.domain.Book;
import site.metacoding.junitproject.domain.BookRepository;
import site.metacoding.junitproject.web.dto.request.BookSaveReqDto;

// 통합테스트 (모든 레이어 - 컨트롤러, 서비스, 레퍼지토리)
// 컨트롤러만 테스트하는것이 아님
@ActiveProfiles("dev") //dev모드일때만 작동
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {
    
    @Autowired
    private TestRestTemplate rt;

    private static HttpHeaders headers;

    private static ObjectMapper om;

    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    public static void init(){
        om = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

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

    @Sql("classpath:db/tableInit.sql") // 시작전에 테이블 초기화를 걸어준다.
    @Test
    public void updateBook() throws Exception{
        //given
        Integer id = 1;
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("spring");
        bookSaveReqDto.setAuthor("메타코딩");

        String body = om.writeValueAsString(bookSaveReqDto);

        //when
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/"+id, HttpMethod.PUT, request, String.class);

        //then
        System.out.println("==================================");
        System.out.println("updateBook_test : " + response.getStatusCode());
        System.out.println("updateBook_test : " + response.getBody());
        System.out.println("==================================");

        DocumentContext dc = JsonPath.parse(response.getBody());
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        System.out.println("=========================================");
        System.out.println("title === "+title);
        System.out.println("author === "+author);
        System.out.println("=========================================");

    }

    @Sql("classpath:db/tableInit.sql") // 시작전에 테이블 초기화를 걸어준다.
    @Test
    public void deleteBook_test(){
        //given
        Integer id = 1;

        //when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/"+id, HttpMethod.DELETE, request, String.class);

        //then
        System.out.println("==================================");
        System.out.println("deleteBook_test : " + response.getStatusCode());
        System.out.println("==================================");
    }

    @Sql("classpath:db/tableInit.sql") // 시작전에 테이블 초기화를 걸어준다.
    @Test
    public void getBookOne_test(){ // 1. getBookOne_test 시작전에 BeforeEach를 시작하는데 !!! 이 모든것 전에 테이블을 초기화를 한번 함.
        // given
        Integer id = 1;

        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/"+id, HttpMethod.GET, request, String.class);

        System.out.println(response.getBody());

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        System.out.println("=========================================");
        System.out.println("title === "+title);
        System.out.println("author === "+author);
        System.out.println("=========================================");
    }

    @Sql("classpath:db/tableInit.sql") // 시작전에 테이블 초기화를 걸어준다.
    @Test
    public void getBookList_test(){
        // given
        // void 데이터준비() 사용

        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/bookList", HttpMethod.GET, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        System.out.println("간다==================" + response.getBody());
        int code = dc.read("$.code");
        String title = dc.read("$.body.items[0].title");

        System.out.println("=========================================");
        System.out.println("code === "+code);
        System.out.println("title === "+title);
        System.out.println("=========================================");
    }

    @Test
    public void saveBook_test() throws JsonProcessingException{
        //given
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("스프링1강");
        bookSaveReqDto.setAuthor("겟인데어");

        
        String body = om.writeValueAsString(bookSaveReqDto);
        System.out.println("===============================");
        System.out.println(body);
        System.out.println("===============================");

        //when
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        
        ResponseEntity<String> response = rt.exchange("/api/v1/insertBook", HttpMethod.POST, request, String.class);

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        System.out.println("=========================================");
        System.out.println("code === "+title);
        System.out.println("title === "+author);
        System.out.println("=========================================");
    }

    
    
}
