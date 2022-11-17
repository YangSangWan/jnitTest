package site.metacoding.junitproject.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.metacoding.junitproject.domain.Book;
import site.metacoding.junitproject.domain.BookRepository;
import site.metacoding.junitproject.util.MailSenderAdapter;
import site.metacoding.junitproject.web.dto.request.BookSaveReqDto;
import site.metacoding.junitproject.web.dto.response.BookListRespDto;
import site.metacoding.junitproject.web.dto.response.BookRespDto;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final MailSenderAdapter mailSender;
    
    // 1. 책등록
    @Transactional(rollbackOn = RuntimeException.class) //RuntimeException가 발생하면 롤백
    public BookRespDto 책등록하기(BookSaveReqDto dto){
        Book bookPS = bookRepository.save(dto.toEntity());
        if(bookPS != null){
            if (!mailSender.send()){
                throw new RuntimeException("메일이 전송되지 않았습니다");
            }
        }
        return bookPS.toDto();
    }

    // 2. 책 목록보기
    public BookListRespDto 책목록보기(){
        List<BookRespDto> dtos = bookRepository.findAll().stream()
        //.map((bookPS) -> new BookRespDto().toDto(bookPS))
        .map(Book::toDto)
        .collect(Collectors.toList());

        BookListRespDto bookListRespDto = BookListRespDto.builder().bookList(dtos).build();
        return bookListRespDto;
        
        
    }

    // 3. 책한건보기
    public BookRespDto 책한건보기(Long id){
        Optional<Book> bookOP = bookRepository.findById(id);
        if(bookOP.isPresent()){ //찾았다면 꺼내서 리턴
            Book bookPS = bookOP.get();
            return bookPS.toDto();
        }else{
            throw new RuntimeException("해당 아이디로 책을 찾을 수 없음");
        }
    }

    // 4. 책삭제
    @Transactional(rollbackOn = RuntimeErrorException.class)
    public void 책삭제하기(Long id){
        bookRepository.deleteById(id);
    }

    // 5. 책수정
    @Transactional(rollbackOn = RuntimeErrorException.class)
    public BookRespDto 책수정하기(Long id, BookSaveReqDto dto){
        Optional<Book> bookOP = bookRepository.findById(id);
        if(bookOP.isPresent()){ //찾았다면 꺼내서 리턴
            Book bookPS = bookOP.get();
            bookPS.update(dto.getTitle(), dto.getAuthor());
            return bookPS.toDto();
        }else{
            throw new RuntimeException("해당 아이디로 책을 찾을 수 없음");
        }
    }

}
