package ge.project.common.recommend;

import ge.project.security.user.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Long> {}

