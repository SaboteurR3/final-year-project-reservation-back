package ge.project.security.token.repository;

import ge.project.security.token.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = """
            select t from Token t inner join User u\s
            on t.user.id = u.id\s
            where u.id = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByToken(String token);

    @Query(value = """
            select t from Token t inner join User u\s
            on t.user.id = u.id\s
            where u.email = :email and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUsername(String email);

    boolean existsByToken(String token);
}
