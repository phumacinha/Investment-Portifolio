package one.digitalinnovation.investment.repository;

import one.digitalinnovation.investment.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    Optional<Investment> findByName(String name);
    List<Investment> findByNameContaining(String name);
}
