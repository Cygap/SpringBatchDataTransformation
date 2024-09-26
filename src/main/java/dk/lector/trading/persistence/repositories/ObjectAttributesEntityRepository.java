package dk.lector.trading.persistence.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import dk.lector.trading.persistence.entities.supplementary.CategoriesEntity;
import dk.lector.trading.persistence.entities.supplementary.ObjectAttributesEntity;
import dk.lector.trading.persistence.entities.supplementary.ObjectTypesEntity;

public interface ObjectAttributesEntityRepository extends CrudRepository<ObjectAttributesEntity, Long> {
	public List<ObjectAttributesEntity> findByObjectIdInAndCategoriesIdIdAndObjectTypesIdId(Collection<Long> objectIds, Long categoriesId, Long objectTypesId);

	public List<ObjectAttributesEntity> findByCategoriesIdAndObjectTypesId(CategoriesEntity categoriesEntity,
			ObjectTypesEntity objectTypesEntity);
	public List<ObjectAttributesEntity> findByCategoriesIdIdAndObjectTypesIdId(Long categoriesId, Long objectTypesId);
	
}
