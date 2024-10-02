package com.finance.sugarmarket.base.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.RestController;
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

@RestController
public class BaseController {

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
		int page = 0;
		int size = 10;

		String pageParam = request.getParameter(FilterFieldConstant.OFFSET);
		if (pageParam != null && !pageParam.isEmpty()) {
			try {
				page = Integer.parseInt(pageParam);
			} catch (NumberFormatException e) {
				page = 0;
			}
		}

		String sizeParam = request.getParameter(FilterFieldConstant.LIMIT);
		if (sizeParam != null && !sizeParam.isEmpty()) {
			try {
				size = Integer.parseInt(sizeParam);
			} catch (NumberFormatException e) {
				size = 10;
			}
		}

		String orderby = request.getParameter(FilterFieldConstant.ORDER_BY);

		String sortColumn = request.getParameter(FilterFieldConstant.SORT_COLUMN);

		String filtersParam = request.getParameter(FilterFieldConstant.FILTERS);

		Sort.Direction direction = (orderby != null && orderby.equalsIgnoreCase("desc")) ? Sort.Direction.DESC
				: Sort.Direction.ASC;

		PageRequest pageRequest = null;
		List<Filter> filters = new ArrayList<>();

		if (sortColumn != null) {
			pageRequest = PageRequest.of(page, size, Sort.by(direction, sortColumn));
		} else {
			pageRequest = PageRequest.of(page, size);
		}

		if (filtersParam != null && !filtersParam.isEmpty()) {
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
}
