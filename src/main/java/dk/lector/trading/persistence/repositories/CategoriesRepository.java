package dk.lector.trading.persistence.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dk.lector.trading.persistence.entities.supplementary.CategoriesEntity;

public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Long> {
	
	public List<CategoriesEntity> findByNameAndContextAndDeletedIsFalse(@Param("name") String name, @Param("context") String context);
	
	public List<CategoriesEntity> findByContextOrderByNameAsc(@Param("context") String context);

	@Query("SELECT DISTINCT c.id "
			+ "FROM CATEGORIES c "
			+ "WHERE c.name IN (:names) "
			+ "AND c.context = :context")
	public List<Long> findIdByNameInAndContext(@Param("names") Collection<String> names, @Param("context") String context);
	
	public Optional<CategoriesEntity> findById(@Param("id") Long id);
}
