package com.finance.sugarmarket.app.config;

import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.util.List;

public class CustomPDFTextStripper extends PDFTextStripper {
    private boolean headerFound = false;
    private static final String HEADER_KEYWORD = "Your_Header_Keyword_Here";

    public CustomPDFTextStripper() throws IOException {
        super();
    }

    @Override
    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
        if (text.contains(HEADER_KEYWORD)) {
            headerFound = true;
            return;
        }

        if (!headerFound) {
            super.writeString(text, textPositions);
        }
    }
}