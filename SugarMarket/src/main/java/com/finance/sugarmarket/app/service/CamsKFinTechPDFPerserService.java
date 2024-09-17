package com.finance.sugarmarket.app.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.finance.sugarmarket.app.config.CustomPDFTextStripper;
import com.finance.sugarmarket.app.dto.CamsKFinTechDto;
import com.finance.sugarmarket.app.model.MutualFund;
import com.finance.sugarmarket.app.model.OrderDetail;
import com.finance.sugarmarket.auth.model.MFUser;
import com.finance.sugarmarket.constants.MFConstants;

@Service
public class CamsKFinTechPDFPerserService extends SaveOrderService {
	private static final Logger log = LoggerFactory.getLogger(CamsKFinTechPDFPerserService.class);

	@Override
	public void processFile(File file, MFUser user, String password) throws Exception {
		try {
			PDDocument document = PDDocument.load(file, password);
			document.setAllSecurityToBeRemoved(true);

			PDFTextStripper stripper = new CustomPDFTextStripper();
			stripper.setSortByPosition(true);

			String text = stripper.getText(document);
			document.close();

			CamsKFinTechDto camsKFinDto = extractText(text);

			// validate user
			validateAndSaveIntoDB(camsKFinDto, user);

			log.info("Upload in DB sucessfully");

		} catch (Exception e) {
			log.error("Error in CamsKFinTechPDFPerserService:processFile()", e);
		}
	}

	private CamsKFinTechDto extractText(String text) {
		CamsKFinTechDto dto = new CamsKFinTechDto();

		String[] lines = text.split("\n");

		// summary
		String emailLine = "Email Id:";
		String emailRegex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
		String phoneLine = "Mobile:";
		String phoneRegex = "\\+\\d{12}";
		String summaryStart = "PORTFOLIO SUMMARY";
		String summaryEnd = "Total";
		String detailStrat = "Folio No:";
		String detailEnd = "Closing Unit Balance:";
		String fundNameRegex = "[A-Z0-9]+-(.+?) - ISIN";
		String dateRegex = "\\d{2}-[A-Za-z]{3}-\\d{4}";

		List<String> summary = new ArrayList<>();
		Map<String, List<String>> details = new HashMap<>();

		int lineNumber = 0;

		while (lineNumber < lines.length) {
			if (dto.getEmailId() == null && lines[lineNumber].startsWith(emailLine)) {
				Pattern pattern = Pattern.compile(emailRegex);
				Matcher matcher = pattern.matcher(lines[lineNumber]);
				while (matcher.find()) {
					String email = matcher.group();
					dto.setEmailId(email);
				}
			} else if (dto.getPhoneNumber() == null && lines[lineNumber].startsWith(phoneLine)) {
				Pattern pattern = Pattern.compile(phoneRegex);
				Matcher matcher = pattern.matcher(lines[lineNumber]);
				while (matcher.find()) {
					String phone = matcher.group();
					dto.setPhoneNumber(phone);
				}
			} else if (dto.getSummary() == null && lines[lineNumber].startsWith(summaryStart)) {
				lineNumber += 4;
				while (!lines[lineNumber].startsWith(summaryEnd)) {
					summary.add(lines[lineNumber++].trim());
				}
			} else if (lines[lineNumber].startsWith(detailStrat)) {
				lineNumber += 2;
				List<String> detail = new ArrayList<>();
				Pattern pattern = Pattern.compile(fundNameRegex);
				Matcher matcher = pattern.matcher(lines[lineNumber]);
				String fundName = null;
				if (matcher.find()) {
					fundName = matcher.group(1).trim();
				}
				lineNumber += 3;
				pattern = Pattern.compile(dateRegex);
				while (!lines[lineNumber].startsWith(detailEnd)) {
					matcher = pattern.matcher(lines[lineNumber]);
					if (matcher.find()) {
						if (!lines[lineNumber].contains("To")) {
							detail.add(lines[lineNumber]);
						}
					}
					lineNumber++;
				}

				if (details.get(fundName) != null)
					detail.addAll(details.get(fundName));
				details.put(fundName, detail);
			}

			lineNumber++;
		}

		dto.setSummary(summary);
		dto.setDetails(details);

		return dto;
	}

	private void validateAndSaveIntoDB(CamsKFinTechDto camsKFinDto, MFUser user) throws Exception {
		if (!camsKFinDto.getEmailId().equals(user.getEmail())
				&& !camsKFinDto.getPhoneNumber().equals(user.getPhonenumber())) {
			throw new Exception("The pdf data is not matched with database");
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

		for (String fund : camsKFinDto.getDetails().keySet()) {
			List<String> values = camsKFinDto.getDetails().get(fund);
			String extractedFundName = extractFundName(fund);
			for (String value : values) {
				OrderDetail ord = new OrderDetail();
				MutualFund mf = new MutualFund();
				mf.setSchemeName(extractedFundName);
				ord.setMutualFund(mf);
				ord.setUser(user);
				if (value.toLowerCase().contains("purchase") || value.toLowerCase().contains("investment")) {
					ord.setSide(MFConstants.BUY);
					String[] parts = value.split("\\s+");
					ord.setDateOfEvent(dateFormat.parse(parts[0]));
					ord.setAmount(Double.parseDouble(parts[parts.length - 4].replaceAll(",", "")));
					ord.setUnits(Double.parseDouble(parts[parts.length - 3].replaceAll(",", "")));
					ord.setNav(Double.parseDouble(parts[parts.length - 2].replaceAll(",", "")));
				} else if (value.toLowerCase().contains("redemption") || value.toLowerCase().contains("redeem")) {
					ord.setSide(MFConstants.SELL);
					String[] parts = value.split("\\s+");
					ord.setDateOfEvent(dateFormat.parse(parts[0]));
					ord.setAmount(Double.parseDouble(
							parts[parts.length - 4].replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(",", "")));
					ord.setUnits(Double.parseDouble(
							parts[parts.length - 3].replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(",", "")));
					ord.setNav(Double.parseDouble(
							parts[parts.length - 2].replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(",", "")));
				} else {
					continue;
				}
				saveInvestment(ord);
			}
		}
	}
	
	public static String extractFundName(String description) {
		String[] excludedKeywords = { "Direct", "formerly", "Growth", "OPTION" };
		description = description.replaceAll("\\([^\\)]*\\)", "");
		String[] parts = description.split("-");
		String cleanedDescription = parts[0].trim();
		for (String keyword : excludedKeywords) {
			cleanedDescription = cleanedDescription.replaceAll("\\b" + keyword + "\\b", "").trim();
		}

		return cleanedDescription;
	}
}
