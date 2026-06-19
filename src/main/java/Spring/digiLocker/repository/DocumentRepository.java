package Spring.digiLocker.repository;

import Spring.digiLocker.entity.Document;
import Spring.digiLocker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document,Long> {
    List<Document> findByOwner(User owner);
}
