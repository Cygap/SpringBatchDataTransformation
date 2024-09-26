package dk.lector.trading.persistence.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import dk.lector.trading.common.fetchAndSaveUtils.dto.ObjectAttributeDTO;
import dk.lector.trading.persistence.entities.businesslogic.ImportLineItemsEntity;

public interface ImportLineItemsRepository
		extends  PagingAndSortingRepository<ImportLineItemsEntity, Long> {
	@Query("SELECT new dk.lector.trading.common.fetchAndSaveUtils.dto.ObjectAttributeDTO("
			+ "ili.regulationCodesId.code, ili.id, ili.createdDate, ili.createdBy) "
			+ "FROM IMPORT_LINE_ITEMS ili"
			+ " WHERE ili.regulationCodesId IS NOT NULL AND ili.regulationCodesId.code <> '' ")
	public Slice<ObjectAttributeDTO> findOAdto(Pageable pageable);
}
