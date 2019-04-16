package ch.monokellabs.lp21.export.xls;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

class Row
{
	public final XSSFRow row;
	private int col;

	public Row(XSSFRow row) {
		this(row, 0);
	}
	
	public Row(XSSFRow row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public XSSFCell add(String value)
	{
		XSSFCell cell = addCell();
		cell.setCellValue(value);
		return cell;
	}
	
	public XSSFCell addCell()
	{
		return row.createCell(col++);
	}
	
	public void mergeColumnsHorizontal(int colCountToMerge)
	{
		int firstCol = col-1;
		int lastCol = firstCol-1+colCountToMerge;
		row.getSheet().addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 
				firstCol, lastCol));
		col = lastCol+1;
	}
	
}