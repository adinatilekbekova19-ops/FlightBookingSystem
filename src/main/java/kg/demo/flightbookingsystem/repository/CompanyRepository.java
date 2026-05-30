package kg.demo.flightbookingsystem.repository;

import kg.demo.flightbookingsystem.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    // Найти компанию по email (для авторизации)
    Company findByEmail(String email);

    // Проверить существование email (для админа при создании)
    boolean existsByEmail(String email);
}
