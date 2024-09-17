package com.finance.sugarmarket.app.dto;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarketData {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	private static final Logger log = LoggerFactory.getLogger(MarketData.class);

	private Map<String, String> meta;
	private List<DateNav> data;
	private TreeMap<Date, Double> navData;
	private String status;

	public Map<String, String> getMeta() {
		return meta;
	}

	public void setMeta(Map<String, String> meta) {
		this.meta = meta;
	}

	public List<DateNav> getData() {
		return data;
	}

	public void setData(List<DateNav> data) {
		this.data = data;
	}

	public TreeMap<Date, Double> getNavData() {
		return navData;
	}

	public void setNavData(TreeMap<Date, Double> navData) {
		this.navData = navData;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void convertToNavData(String apiData, String schemeCode) throws Exception {
		try {
		this.navData = new TreeMap<>();
		for (DateNav nav : this.data) {
			try {
				this.navData.put(sdf.parse(nav.getDate()), Double.valueOf(nav.getNav()));
			}
			catch (Exception e) {
				throw new Exception("error in" + nav.getDate() + " -- " + nav.getNav(), e);
			}
		}
		}
		catch (Exception e) {
			FileWriter fileWriter;
			try {
				fileWriter = new FileWriter("C:\\ZZZ\\Tmp\\" + schemeCode + ".txt");
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				bufferedWriter.write(apiData);
				bufferedWriter.close();
			} catch (IOException e1) {
				log.error("error in getHistoricalData(): ", e1);
			}
		}
		this.data = null;
	}
}