package com.finance.sugarmarket.base.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.base.dto.Operands;
import com.finance.sugarmarket.base.enums.Operators;
import com.finance.sugarmarket.constants.FieldConstant;

import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;

abstract public class SpecificationService<T> {

	public Specification<T> getSpecificationFilters(List<Filter> filters, Map<String, String> filterMap)
			throws Exception {
		Specification<T> specificationFilters = Specification.where(null);
		for (Filter filter : filters) {
			Specification<T> partFilter = Specification.where(null);
			for (Operands operand : filter.getOperands()) {
				Specification<T> spec = createSpecificationFromFilter(operand, filterMap);
				if (filter.getOperator().equals(Operators.AND)) {
					partFilter = partFilter.and(spec);
				} else if (filter.getOperator().equals(Operators.OR)) {
					partFilter = partFilter.or(spec);
				} else {
					throw new Exception(filter.getOperator() + " operator is not allowed to join tow conditions.");
				}
			}
			specificationFilters = specificationFilters.and(partFilter);
		}

		return specificationFilters;
	}

	public Specification<T> createSpecificationFromFilter(Operands operand, Map<String, String> filterMap) {
		String column = filterMap.get(operand.getColumn()) != null ? filterMap.get(operand.getColumn())
				: operand.getColumn();

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

			switch (operand.getOperation()) {
			case LIKE:
				return builder.like(path.as(String.class), "%" + operand.getValue() + "%");
			case EQUAL:
				return builder.equal(path, operand.getValue());
			case NOT_EQUAL:
				return builder.notEqual(path, operand.getValue());
			default:
				throw new UnsupportedOperationException("Operation not supported: " + operand.getOperation());
			}
		};
	}

	public Specification<T> getAuditSpecificationFilters(Map<String, String> filterMap, Long id, Long userId)
			throws Exception {
		List<Operands> operands = new ArrayList<>();
		operands.add(new Operands(FieldConstant.ID, Operators.EQUAL, id.toString()));
		operands.add(new Operands(FieldConstant.USER_ID, Operators.EQUAL, userId.toString()));
		List<Filter> filters = new ArrayList<>();
		filters.add(new Filter(Operators.AND, operands));
		return getSpecificationFilters(filters, filterMap);
	}
}
