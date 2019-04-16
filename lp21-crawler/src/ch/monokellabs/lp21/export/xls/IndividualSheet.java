package ch.monokellabs.lp21.export.xls;

import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import ch.monokellabs.lp21.Kompetenz;
import ch.monokellabs.lp21.export.xls.meta.PrintOptions;

/**
 * Progress of the child and planning for future semesters
 */
public class IndividualSheet
{
	private final XSSFSheet sheet;
	private final Style style;
	
	public IndividualSheet(XSSFSheet sheet) {
		this.sheet = sheet;
		this.style = new Style(sheet.getWorkbook());
		new PrintOptions(sheet).optimize();
	}
	
	private static final int MAX_ZYKLUS = 3;
	private static final int MAX_SEMESTER = MAX_ZYKLUS*6;
	private static final int MAX_STATUS = MAX_SEMESTER*2;
	
	public void fill(Collection<Kompetenz> kompetenzen)
	{
		zyklusRow(3);
		semesterRow(4);
		statusRow(5);
		
		kompetenzenGridEast(6, kompetenzen);
	}

	private void zyklusRow(int rowNum) {
		Row zyklusRow = new Row(sheet.createRow(rowNum), 0);
		zyklusRow.add("Zyklus");
		for(int zyklus=0; zyklus<=MAX_ZYKLUS; zyklus++)
		{
			XSSFCell zCell = zyklusRow.addCell();
			zCell.setCellValue(zyklus);
			zCell.setCellStyle(style.getZyklusStyle(zyklus));
			if (zyklus != 0)
			{
				int mergeCols = MAX_STATUS/MAX_ZYKLUS;
				zyklusRow.mergeColumnsHorizontal(mergeCols);
			}
		}
	}

	private void semesterRow(int rowNum) {
		Row semesterRow = new Row(sheet.createRow(rowNum), 0);
		semesterRow.add("Semester");
		for(int semester=0; semester<=MAX_SEMESTER; semester++)
		{
			XSSFCell sCell = semesterRow.addCell();
			sCell.setCellValue(semester);
			sCell.setCellStyle(style.topCenter);
			if (semester != 0)
			{
				int mergeCols = MAX_STATUS/MAX_SEMESTER;
				semesterRow.mergeColumnsHorizontal(mergeCols);
			}
		}
	}

	private void statusRow(int rowNum) {
		Row statusRow = new Row(sheet.createRow(rowNum), 0);
		statusRow.add("Status");
		for(int status=0; status<=MAX_STATUS; status++)
		{
			XSSFCell cell = statusRow.addCell();
			cell.setCellStyle(style.topCenter);
			boolean even = status%2 == 0;
			if (even)
			{
				cell.setCellValue("E");
			}
			else
			{
				cell.setCellValue("P");
			}
		}
		for(int col = 1; col<=MAX_STATUS+1; col++)
		{ // make small
			sheet.setColumnWidth(col, 2*256);
		}
	}
	
	private static final List<String> RAW_KP_COLUMNS = java.util.Arrays.asList(
			"J", // zyklus
			"K", // stufencode
			"L" // stufentext
			);
	
	private void kompetenzenGridEast(int rowNum, Collection<Kompetenz> kompetenzen) {
		int offsetToKpRaw = -3;
		Row kpHeader = new Row(sheet.getRow(rowNum-1), MAX_STATUS+2);
		kpHeader.add("Erreicht");
		kpHeader.add("Zyklus");
		kpHeader.add("S Code");
		kpHeader.add("Komptenzstuffe");

		Integer stufen = kompetenzen.stream()
				.map(k -> k.stufen.size())
				.reduce(0, Integer::sum);
		int firstFilterCol = MAX_STATUS+2;
		sheet.setAutoFilter(new CellRangeAddress(
				rowNum-1, rowNum-1+stufen, firstFilterCol, firstFilterCol+3));
		
		for(int row=rowNum; row<stufen; row++)
		{
			Row kpRow = new Row(sheet.createRow(row), MAX_STATUS+2);
			XSSFCell erreicht = kpRow.addCell();
			erreicht.setCellType(CellType.FORMULA);
			int formularRow = row+1;
			erreicht.setCellFormula(evaluateMaxErreicht(formularRow));
			for(String kpColRef : RAW_KP_COLUMNS)
			{
				XSSFCell cell = kpRow.addCell();
				cell.setCellType(CellType.FORMULA);
				cell.setCellFormula("RAW!"+kpColRef+(row+offsetToKpRaw));
			}
		}
	}

	private static String evaluateMaxErreicht(int formularRow) {
		String rowName = "B"; // start here
		StringBuilder formula = new StringBuilder("MAX(");
		for(int i=0; i<=MAX_SEMESTER; i++)
		{
			formula.append(rowName).append(formularRow);
			if (i<MAX_SEMESTER)
			{
				formula.append(",");
			}
			rowName = getNextName(rowName, 2);
		}
		formula.append(")");
		return formula.toString();
	}

	public static String getNextName(String rowName, int offset) {
		StringBuilder next = new StringBuilder();
		if (rowName.length()>1)
		{
			String prefix = rowName.substring(0, rowName.length()-1);
			next.append(prefix); 
		}
		char lastChar = rowName.charAt(rowName.length()-1);
		char nextChar = (char)((int)lastChar+offset);
		if (nextChar > 'Z')
		{
			next.append('A');
			int diff = nextChar - 1 - (int)'Z';
			nextChar = (char)((int)'A'+diff);
		}
		return next.append(nextChar).toString();
	}
}