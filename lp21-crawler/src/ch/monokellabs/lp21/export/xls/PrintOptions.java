package ch.monokellabs.lp21.export.xls;

import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class PrintOptions {

	private XSSFSheet sheet;

	public PrintOptions(XSSFSheet sheet) {
		this.sheet = sheet;
	}

	public void optimize()
	{
		sheet.getPrintSetup().setLandscape(true);
		sheet.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
		
		sheet.getFooter().setCenter("(c)opyright by Privatunterricht Wermelinger");
	}
	
}
