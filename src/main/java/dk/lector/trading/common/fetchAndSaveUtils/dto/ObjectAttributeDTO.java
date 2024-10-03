package dk.lector.trading.common.fetchAndSaveUtils.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import dk.lector.trading.persistence.entities.clientmaster.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class ObjectAttributeDTO implements Comparable<ObjectAttributeDTO> {

	private Long id; //ObjectAttribute id
	private String attributeValue;

	private Long referencedId;

	private String categoryName;
	private String categoryContext;
	
	private LocalDateTime createdDate;
	private UsersEntity createdBy;
	

	public ObjectAttributeDTO(Long id, String attributeValue, Long referencedId) {
		this.id = id;
		this.attributeValue = attributeValue;
		this.referencedId = referencedId;
	}

	public ObjectAttributeDTO(String attributeValue, Long referencedId, LocalDateTime createdDate, UsersEntity createdBy) {
		this.attributeValue = attributeValue;
		this.referencedId = referencedId;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
	}
	
	@Override
	public int compareTo(ObjectAttributeDTO o) {
		if(o == null) {
			return -1;
		}
		return ObjectUtils.compare(this.attributeValue, o.attributeValue);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if(o != null && o instanceof ObjectAttributeDTO) {
			ObjectAttributeDTO otherDto = (ObjectAttributeDTO)o;
			return Objects.equals(this.attributeValue, otherDto.attributeValue);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		if(attributeValue != null) {
			return attributeValue.hashCode();
		}
		return 0;
	}
	
	public static Collection<String> getAttributeValues(Collection<ObjectAttributeDTO> attributeDtos) {
		if(attributeDtos != null && !attributeDtos.isEmpty()) {
			final Collection<String> stringValues = new ArrayList<>(attributeDtos.size());
			for(ObjectAttributeDTO attributeDto : attributeDtos) {
				stringValues.add(attributeDto.attributeValue);
			}
			return stringValues;
		}
		return null;
	}
	public static String buildSummary(Collection<ObjectAttributeDTO> attributeDtos, String delimiter, Number maxLength) {
		if(attributeDtos != null && !attributeDtos.isEmpty()) {
			return sortedSummaryString(getAttributeValues(attributeDtos), delimiter, false, maxLength);
		}
		return null;
	}
	
	public static String sortedSummaryString(Collection<String> values, String delimiter, boolean ensureUnique, Number maxLength) {
		if(values != null) {
			final ArrayList<String> orderedValues = new ArrayList<>((ensureUnique && !(values instanceof Set)) ? new HashSet<>(values) : values);
			orderedValues.removeAll(Arrays.asList("", null));
			if(!orderedValues.isEmpty()) {
				Collections.sort(orderedValues);
				String summaryString = String.join(delimiter, orderedValues);
				if(maxLength != null && maxLength.intValue() > 0 && summaryString.length() > maxLength.intValue()) {
					summaryString = summaryString.substring(0, maxLength.intValue());
					while(summaryString.endsWith(delimiter)) {
						summaryString = summaryString.substring(0, summaryString.length()-delimiter.length());
					}
					summaryString = summaryString + "...";
				}
				return summaryString;
			}
		}
		return null;
	}
}
