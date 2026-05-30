package kg.demo.flightbookingsystem.repository;

import kg.demo.flightbookingsystem.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findByEmail(String email);

    boolean existsByEmail(String email);
}
