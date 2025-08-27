package iri.elearningapi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import iri.elearningapi.model.Region;

public interface RegionRepository extends JpaRepository<Region, Integer> {
	List<Region> findAllByOrderByNomAsc();
}
