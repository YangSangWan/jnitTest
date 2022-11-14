package site.metacoding.junitproject.web.dto.response;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Builder
public class BookRespDto{
    private Long id;
    private String title;
    private String author;
    public BookRespDto(Long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    
}