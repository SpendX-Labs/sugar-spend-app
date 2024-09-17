package com.finance.sugarmarket.app.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.finance.sugarmarket.app.dto.MarketData;
import com.finance.sugarmarket.app.model.MutualFund;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MarketDataUtil {

	public static final String BASE_URL = "https://api.mfapi.in/mf";
	private static final Logger log = LoggerFactory.getLogger(MarketDataUtil.class);

	public static List<MutualFund> getMFList() {
		List<MutualFund> data = new ArrayList<>();
		try {
			String apiData = getDataFromURL(BASE_URL);
			Gson g = new Gson();
			TypeToken<List<MutualFund>> token = new TypeToken<List<MutualFund>>() {
			};
			data = g.fromJson(apiData, token);
		} catch (Exception e) {
			log.error("error in getLatestData() ", e);
		}
		return data;
	}

	public static MarketData getHistoricalData(String schemeCode) {
		MarketData data = new MarketData();
		String apiData = null;
		try {
			apiData = getDataFromURL(BASE_URL + "/" + schemeCode);
			data = generateMapData(apiData, schemeCode);
		} catch (Exception e) {
			log.error("error in getLatestData() for " + schemeCode, e);
		}
		return data;
	}

	public static MarketData getLatestData(String schemeCode) {
		MarketData data = new MarketData();
		try {
			String apiData = getDataFromURL(BASE_URL + "/" + schemeCode + "/latest");
			data = generateMapData(apiData, schemeCode);
		} catch (Exception e) {
			log.error("error in getLatestData() ", e);
		}
		return data;
	}

	private static MarketData generateMapData(String apiData, String schemeCode) throws Exception {
		Gson g = new Gson();
		MarketData fundetails = g.fromJson(apiData, MarketData.class);
		fundetails.convertToNavData(apiData, schemeCode);
		return fundetails;
	}

	private static String getDataFromURL(String apiUrl) throws Exception {
		// Create a URL object
		URL url = new URL(apiUrl);

		// Open a connection to the URL
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// Set the request method to GET
		connection.setRequestMethod("GET");

		// Get the response code
		int responseCode = connection.getResponseCode();

		// Check if the response code is successful
		if (responseCode == 200) {
			// Get the response input stream
			InputStream inputStream = connection.getInputStream();

			// Create a scanner to read the input stream
			Scanner scanner = new Scanner(inputStream);

			// Read the data from the input stream
			String data = scanner.nextLine();
			scanner.close();
			return data;
		} else {
			throw new Exception("failed to load data, mfapi connection refused.");
		}
	}
}
