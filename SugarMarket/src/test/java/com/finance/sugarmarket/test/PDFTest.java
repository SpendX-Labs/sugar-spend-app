package com.finance.sugarmarket.test;

import com.finance.sugarmarket.app.service.CamsKFinTechPDFPerserService;

import java.io.File;

public class PDFTest {
    public static void main(String[] args) {
        try {
            File file = new File("C:\\ZZZ\\CAMS_SUBHAM.pdf");
            CamsKFinTechPDFPerserService ob = new CamsKFinTechPDFPerserService();
            ob.processFile(file, null, "Limbo123");

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
