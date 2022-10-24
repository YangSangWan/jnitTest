package site.metacoding.junitproject.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest //DB와 관련된 컴토넌트만 메모리에 로딩
public class BookRepositoryTest {
    
    @Autowired // DI
    private BookRepository bookRepository;

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

    //3. 책 한건 보기

    //4. 책 수정

    //5. 책 삭제
}
