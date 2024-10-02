package com.finance.sugarmarket.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.sugarmarket.auth.model.MFRole;
import com.finance.sugarmarket.auth.repo.MapRoleUserRepo;
import com.finance.sugarmarket.auth.service.JwtCacheService;
import com.finance.sugarmarket.auth.service.JwtService;
import com.finance.sugarmarket.base.dto.Filter;
import com.finance.sugarmarket.base.enums.FilterOperation;
import com.finance.sugarmarket.constants.FilterFieldConstant;

import jakarta.servlet.http.HttpServletRequest;

abstract public class BaseController {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private MapRoleUserRepo mapRoleUserRepo;
	@Autowired
	private JwtCacheService jwtCacheService;
	@Autowired
	private ObjectMapper objectMapper;

	private String getToken() {
		String token = null;
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		if (requestAttributes != null) {
			token = requestAttributes.getRequest().getHeader("Authorization");
		}
		return jwtCacheService.extractJwtFromHeader(token);
	}

	protected String getUserName() {
		return jwtService.extractUsername(getToken());
	}

	protected Long getUserId() {
		return Long.parseLong(jwtService.extractUserId(getToken()));
	}

	protected void checkAdminRole() throws Exception {
		if (!mapRoleUserRepo.findByUser(getUserId()).getRole().getRoleName().equals(MFRole.Role.admin.name())) {
			throw new Exception("The loggedin user is not admin.");
		}
	}

	protected void checkCustomerRole() throws Exception {
		if (!mapRoleUserRepo.findByUser(getUserId()).getRole().getRoleName().equals(MFRole.Role.customer.name())) {
			throw new Exception("The loggedin user is not customer.");
		}
	}

	protected Pair<PageRequest, List<Filter>> getPageRequestAndFilters() {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		// Pages and offset limit
		int offset = 0;
		int limit = 10;

		String offSetParam = request.getParameter(FilterFieldConstant.OFFSET);
		if (StringUtils.isNotEmpty(offSetParam)) {
			try {
				offset = Integer.parseInt(offSetParam);
			} catch (NumberFormatException e) {
				offset = 0;
			}
		}

		String limitParam = request.getParameter(FilterFieldConstant.LIMIT);
		if (StringUtils.isNotEmpty(limitParam)) {
			try {
				limit = Integer.parseInt(limitParam);
			} catch (NumberFormatException e) {
				limit = 10;
			}
		}

		// Sort by column
		Map<String, Sort.Direction> sortColumnMap = getDefaultSortColumns(request);

		PageRequest pageRequest = null;

		if (sortColumnMap.isEmpty()) {
			pageRequest = PageRequest.of(offset, limit);
		} else {
			List<Sort.Order> orders = new ArrayList<>();
			for (String col : sortColumnMap.keySet()) {
				orders.add(new Sort.Order(sortColumnMap.get(col), col));
			}
			Sort sort = Sort.by(orders);
			pageRequest = PageRequest.of(offset, limit, sort);
		}

		// Filters
		List<Filter> filters = new ArrayList<>();
		String filtersParam = request.getParameter(FilterFieldConstant.FILTERS);

		if (StringUtils.isNotEmpty(filtersParam)) {
			try {
				filters = objectMapper.readValue(filtersParam, new TypeReference<List<Filter>>() {
				});
			} catch (Exception e) {
				filters = new ArrayList<>();
			}
		}

		filters.add(new Filter(FilterFieldConstant.USER_ID, FilterOperation.EQUAL, getUserId().toString()));

		return Pair.of(pageRequest, filters);
	}

	public Map<String, Sort.Direction> getDefaultSortColumns(HttpServletRequest request) {
		String orderByColumn = request.getParameter(FilterFieldConstant.SORT_COLUMN);
		Map<String, Sort.Direction> map = new HashMap<>();
		if (StringUtils.isEmpty(orderByColumn))
			return map;
		String orderby = request.getParameter(FilterFieldConstant.ORDER_BY);
		Sort.Direction direction = (orderby != null && orderby.equalsIgnoreCase(FilterFieldConstant.DESC))
				? Sort.Direction.DESC
				: Sort.Direction.ASC;
		map.put(orderByColumn, direction);
		return map;
	}
}
