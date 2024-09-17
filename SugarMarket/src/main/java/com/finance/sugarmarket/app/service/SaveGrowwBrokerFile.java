package com.finance.sugarmarket.app.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.app.model.MutualFund;
import com.finance.sugarmarket.app.model.OrderDetail;
import com.finance.sugarmarket.auth.model.MFUser;

@Service
public class SaveGrowwBrokerFile extends SaveOrderService {
	
	private static final Logger log = LoggerFactory.getLogger(SaveGrowwBrokerFile.class);

	@Override
	public void processFile(File file, MFUser user, String password) throws Exception {
		try(InputStream inputStream = new FileInputStream(file);
			Workbook workbook = new HSSFWorkbook(inputStream)){
			// Assuming you want to read data from the second sheet (index 1)
			Sheet sheet = workbook.getSheetAt(1);

			int skipLines = 4;
			int headerLines = 1;

			Map<String, String> map = new TreeMap<>();

			map.put("Scheme Name", "mutualFund.schemeName");
			map.put("Transaction Type", "side");
			map.put("Units", "units");
			map.put("NAV", "nav");
			map.put("Amount", "amount");
			map.put("Date", "dateOfEvent");

			List<BiFunction<String, OrderDetail, OrderDetail>> functionList = new ArrayList<>();

			for (Row row : sheet) {
				if (skipLines > 0) {
					skipLines--;
					continue;
				}

				if (headerLines > 0) {
					for (Cell cell : row) {
						if (!map.containsKey(cell.getStringCellValue())) {
							throw new Exception("File Is not valid");
						}
						functionList.add(createOrderDetailFunction(map.get(cell.getStringCellValue())));
					}
					headerLines--;
					continue;
				}

				OrderDetail orderDetail = new OrderDetail();
				int index =0;
				for (Cell cell : row) {
					String cellValue = null;
					if(cell.getCellType() == CellType.NUMERIC) {
						cellValue = Double.toString(cell.getNumericCellValue());
					}
					else {
						cellValue = cell.getStringCellValue();
					}
					functionList.get(index).apply(cellValue, orderDetail);
					index++;
				}
				orderDetail.setUser(user);
				saveInvestment(orderDetail);
			}
			
			
			log.info("File upload success:" + file.getName());
			
			
		} catch (Exception e) {
			log.error("Error While Uploading data: ",e);
		}
	}

	BiFunction<String, OrderDetail, OrderDetail> createOrderDetailFunction(String column) {
		return (value, orderDetail) -> {
			if(column.equals("mutualFund.schemeName")) {
				MutualFund fund = new MutualFund();
				value = value.replaceAll("\\s+", " ");
				fund.setSchemeName(value);
				orderDetail.setMutualFund(fund);
			}
			else if(column.equals("side")) {
				orderDetail.setSide(value.equals("PURCHASE") ? "Buy" : "Sell");
			}
			else if(column.equals("units")) {
				orderDetail.setUnits(Double.valueOf(value));
			}
			else if(column.equals("nav")) {
				orderDetail.setNav(Double.valueOf(value));
			}
			else if(column.equals("amount")) {
				value = value.replaceAll(",", "");
				orderDetail.setAmount(Double.valueOf(value));
			}
			else if(column.equals("dateOfEvent")) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
				try {
					orderDetail.setDateOfEvent(sdf.parse(value));
				} catch (Exception e) {
					throw new IllegalArgumentException(value + " is invalid date");
				}
			}
			return orderDetail;
		};
	}
}