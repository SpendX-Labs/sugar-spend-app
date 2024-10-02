package com.finance.sugarmarket.base.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.base.enums.FilterOperation;
import com.finance.sugarmarket.constants.FilterFieldConstant;

import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;

abstract public class SpecificationService<T> {
	
	public Specification<T> getSpecificationFilters(List<Filter> filters, Map<String, String> filterMap) {
		Specification<T> specificationFilters = Specification.where(null);
		for (Filter filter: filters) {
			Specification<T> spec = createSpecificationFromFilter(filter, filterMap);
			specificationFilters = specificationFilters.and(spec);
		}
		
		return specificationFilters;
	}

	public Specification<T> createSpecificationFromFilter(Filter filter, Map<String, String> filterMap) {
		String column = filterMap.get(filter.getColumn()) != null ? filterMap.get(filter.getColumn())
				: filter.getColumn();

		String[] parts = column.split("\\.");

		return (root, query, builder) -> {
			Path<?> path = root;

			for (int i = 0; i < parts.length; i++) {
				if (i < parts.length - 1) {
					path = ((From<?, ?>) path).join(parts[i]);
				} else {
					path = path.get(parts[i]);
				}
			}

			switch (filter.getOperation()) {
			case LIKE:
				return builder.like(path.as(String.class), "%" + filter.getValue() + "%");
			case EQUAL:
				return builder.equal(path, filter.getValue());
			case NOT_EQUAL:
				return builder.notEqual(path, filter.getValue());
			default:
				throw new UnsupportedOperationException("Operation not supported: " + filter.getOperation());
			}
		};
	}
	
	public Specification<T> getAuditSpecificationFilters(Map<String, String> filterMap, Long id, Long userId) {
		List<Filter> filters = new ArrayList<>();
		filters.add(new Filter(FilterFieldConstant.ID, FilterOperation.EQUAL, id.toString()));
		filters.add(new Filter(FilterFieldConstant.USER_ID, FilterOperation.EQUAL, userId.toString()));
 		return getSpecificationFilters(filters, filterMap);
	}
}
