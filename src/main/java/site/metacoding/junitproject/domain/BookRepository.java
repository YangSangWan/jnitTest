package site.metacoding.junitproject.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//JpaRepository 상속하면 자동으로 빈 등록됨
public interface BookRepository extends JpaRepository<Book, Long>{
    
}
