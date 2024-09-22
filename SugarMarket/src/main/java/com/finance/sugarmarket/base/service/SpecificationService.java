package com.finance.sugarmarket.base.service;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.finance.sugarmarket.base.dto.Filter;

import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;

public class SpecificationService<T> {

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
}
