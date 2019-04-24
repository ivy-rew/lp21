package ch.monokellabs.lp21.export.xls;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

public class Style
{
	public final CellStyle top;
	public final CellStyle topCenter;
	public final CellStyle topAutoBreak;
	public final CellStyle codeShort;
	
	public final CellStyle z1;
	public final CellStyle z2;
	public final CellStyle z3;
	
	public final CellStyle grundanspruch;
	public final CellStyle rotate90;
	
	public Style(Workbook wb)
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
		
		
		// Z1 = #E39B2D;
		z1 = wb.createCellStyle();
		z1.cloneStyleFrom(topCenter);
		z1.setFillBackgroundColor(HSSFColorPredefined.LIGHT_ORANGE.getIndex());
		z1.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);
		// Z2 = #2F8FCE
		z2 = wb.createCellStyle();
		z2.cloneStyleFrom(z1);
		z2.setFillBackgroundColor(HSSFColorPredefined.LIGHT_BLUE.getIndex());
		// Z3 = #97C941
		z3 = wb.createCellStyle();
		z3.cloneStyleFrom(z1);
		z3.setFillBackgroundColor(HSSFColorPredefined.LIGHT_GREEN.getIndex());
		
		grundanspruch = wb.createCellStyle();
		grundanspruch.setFillBackgroundColor(HSSFColorPredefined.BROWN.getIndex());
		grundanspruch.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);
		
		rotate90 = wb.createCellStyle();
		rotate90.setRotation((short)90);
		rotate90.setAlignment(HorizontalAlignment.LEFT);
		rotate90.setVerticalAlignment(VerticalAlignment.BOTTOM);
	}
	
	public CellStyle getZyklusStyle(int zyklus) {
		if (zyklus == 1)
		{
			return z1;			
		}
		else if (zyklus == 2)
		{
			return z2;
		}
		else if (zyklus == 3)
		{
			return z3;
		}
		return topCenter;
	}
}