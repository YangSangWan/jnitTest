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
import site.metacoding.junitproject.util.MailSenderStub;
import site.metacoding.junitproject.web.dto.BookRespDto;
import site.metacoding.junitproject.web.dto.BookSaveReqDto;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final MailSenderStub mailSender;
    
    // 1. 책등록
    @Transactional(rollbackOn = RuntimeException.class) //RuntimeException가 발생하면 롤백
    public BookRespDto 책등록하기(BookSaveReqDto dto){
        Book bookPS = bookRepository.save(dto.toEntity());
        if(bookPS != null){
            if (!mailSender.send()){
                throw new RuntimeException("메일이 전송되지 않았습니다");
            }
        }
        return new BookRespDto().toDto(bookPS);
    }

    // 2. 책 목록보기
    public List<BookRespDto> 책목록보기(){
        return bookRepository.findAll().stream()
        //.map(new BookRespDto()::toDto)  //이거 1.8 메서드 참조라는 문법...
        .map((bookPS) -> new BookRespDto().toDto(bookPS))
        .collect(Collectors.toList());
        
    }

    // 3. 책한건보기
    public BookRespDto 책한건보기(Long id){
        Optional<Book> bookOP = bookRepository.findById(id);
        if(bookOP.isPresent()){ //찾았다면 꺼내서 리턴
            return new BookRespDto().toDto(bookOP.get());
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
    public void 책수정하기(Long id, BookSaveReqDto dto){
        Optional<Book> bookOP = bookRepository.findById(id);
        if(bookOP.isPresent()){ //찾았다면 꺼내서 리턴
            Book bookPS = bookOP.get();
            //bookPS.update(dto.getTitle(), dto.getAuthor());
        }else{
            throw new RuntimeException("해당 아이디로 책을 찾을 수 없음");
        }
    }

}
