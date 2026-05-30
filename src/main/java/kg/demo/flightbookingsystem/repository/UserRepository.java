package kg.demo.flightbookingsystem.repository;


import kg.demo.flightbookingsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Найти пользователя по email (для авторизации)
    User findByEmail(String email);

    // Проверить существование email (для регистрации)
    boolean existsByEmail(String email);
}