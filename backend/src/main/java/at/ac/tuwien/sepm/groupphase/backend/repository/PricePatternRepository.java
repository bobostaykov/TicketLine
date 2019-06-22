package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.PricePattern;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricePatternRepository extends JpaRepository<PricePattern, Long> {
}
