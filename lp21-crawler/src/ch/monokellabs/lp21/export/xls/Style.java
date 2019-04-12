package ch.monokellabs.lp21.export.xls;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

class Style
{
	public final CellStyle top;
	public final CellStyle topCenter;
	public final CellStyle topAutoBreak;
	public final CellStyle codeShort;
	
	Style(Workbook wb)
	{
		top = wb.createCellStyle();
		top.setVerticalAlignment(VerticalAlignment.TOP);
		
		topCenter = wb.createCellStyle();
		topCenter.setVerticalAlignment(VerticalAlignment.TOP);
		topCenter.setAlignment(HorizontalAlignment.CENTER);
		
		topAutoBreak = wb.createCellStyle();
		topAutoBreak.cloneStyleFrom(top);
		topAutoBreak.setWrapText(true);
		
		codeShort = wb.createCellStyle();
		codeShort.setVerticalAlignment(VerticalAlignment.TOP);
		codeShort.setAlignment(HorizontalAlignment.RIGHT);
	}
}