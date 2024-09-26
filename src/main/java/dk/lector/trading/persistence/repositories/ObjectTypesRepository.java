package dk.lector.trading.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import dk.lector.trading.persistence.entities.supplementary.ObjectTypesEntity;

public interface ObjectTypesRepository extends JpaRepository<ObjectTypesEntity, Long> {

	List<ObjectTypesEntity> findByReferenceTableIsNullAndObjectType(@Param("objectType") String objectType);
	
	List<ObjectTypesEntity> findByReferenceTableAndObjectType(@Param("referenceTable") String referenceTable,@Param("objectType") String objectType);

}
