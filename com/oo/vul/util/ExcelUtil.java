package com.oo.vul.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

	private static Log log=LogFactory.getLog(ExcelUtil.class);
	
	public static List<List<List<String>>> getExcelContent(String filePath)
			throws Exception {

		List<List<List<String>>> resultList = new ArrayList<List<List<String>>>();
		File file = new File(filePath);
		Workbook book = null;
		if (filePath.endsWith(".xls")) {
			book = new HSSFWorkbook(new FileInputStream(file));
		} else {
			book = new XSSFWorkbook(new FileInputStream(file));
		}
		int sheets = book.getNumberOfSheets();
		for (int i = 0; i < sheets; i++) {
			Sheet sheet = book.getSheetAt(i);
			int lastRowNum = sheet.getLastRowNum();
			// int firstRowNum = sheet.getFirstRowNum();
			List<List<String>> sheetList = new ArrayList<List<String>>();
			resultList.add(sheetList);
			for (int j = 0; j < lastRowNum + 1; j++) {
				Row row = sheet.getRow(j);
				if (row == null) {
					continue;
				}
				// int firstCellNum = row.getFirstCellNum();
				int lastCellNum = row.getLastCellNum();
				List<String> rowList = new ArrayList<String>();
				sheetList.add(rowList);
				for (int k = 0; k < lastCellNum + 1; k++) {
					Cell cell = row.getCell(k);
					if (cell == null) {
						rowList.add("");
						continue;
					}
					String cellValue = getStringCellValue(cell);
					rowList.add(cellValue);
				}
			}
		}
		for (int i = 0; i < resultList.size(); i++) {
			List<List<String>> sheet = resultList.get(i);
			out: for (Iterator<List<String>> iter = sheet.iterator(); iter
					.hasNext();) {
				List<String> row = iter.next();
				int sum = 0;
				for (int k = 0; k < row.size(); k++) {
					String cell = row.get(k);
					if (cell == null || "".equals(cell)) {
						sum++;
					} else {
						continue out;
					}
				}
				if (sum == row.size()) {
					iter.remove();
				}
			}
		}
		return resultList;
	}

	private static String getStringCellValue(Cell cell) throws Exception {
		String cellValue = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			cellValue = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				Date dateCellValue = cell.getDateCellValue();
				cellValue = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
						.format(dateCellValue);
			} else {
				cellValue = String.valueOf(new DecimalFormat("#").format(cell
						.getNumericCellValue()));
			}
			break;
		case Cell.CELL_TYPE_FORMULA:
			cellValue = cell.getCellFormula();
			break;
		case Cell.CELL_TYPE_ERROR:
			cellValue = new String(new byte[] { cell.getErrorCellValue() });
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			cellValue = cell.getBooleanCellValue() + "";
			break;
		}
		if (cellValue.matches("\\d+\\.0+")) {
			cellValue = cellValue.substring(0, cellValue.lastIndexOf("."));
		}
		return cellValue.trim();
	}
	public static void exportExcel(List<List<String>> dataList,String fileName){
		 try {
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheet = workbook.createSheet("result");
				HSSFCellStyle headerStyle = getHeaderStyle(workbook);
				HSSFCellStyle cellStyle = getCellStyle(workbook);
				HSSFCellStyle whiteBlueHeaderStyle=getCustomStyle(workbook,HSSFColor.LIGHT_BLUE.index,HSSFColor.WHITE.index);
				HSSFCellStyle yellowBlackCellStyle=getCustomStyle(workbook,HSSFColor.YELLOW.index,HSSFColor.BLACK.index);
				for (int i = 0; dataList != null && i < dataList.size(); i++) {
					List<String> data = dataList.get(i);
					HSSFRow row = sheet.createRow(i);
					//修正 设备负责人的列 列头用白字蓝底   列用黄色底色
					for (int j = 0; j < data.size(); j++) {
						String value = data.get(j);
						HSSFCell cell = row.createCell(j);
						HSSFRichTextString text = new HSSFRichTextString(
								value == null ? "" : value);
						cell.setCellValue(text);
						if (i == 0) {
							cell.setCellStyle(headerStyle);
							sheet.setColumnWidth(j, 5000);
							if(j==20){
								cell.setCellStyle(whiteBlueHeaderStyle);
							}
						}else{
							cell.setCellStyle(cellStyle);
							if(j==20){
								cell.setCellStyle(yellowBlackCellStyle);
							}
						}
					}
				}
				FileOutputStream output = new FileOutputStream(fileName);
				workbook.write(output);
				output.close();
		} catch (Exception e) {
		  	log.error(LogError.getDetailError(e));
		}
	}
	private static HSSFCellStyle getHeaderStyle(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColor.GOLD.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		style.setFont(font);
		return style;
	}
	private static HSSFCellStyle getCustomStyle(HSSFWorkbook workbook,short bkColor,short fontColor) {
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(bkColor);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = workbook.createFont();
		font.setColor(fontColor);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		style.setFont(font);
		return style;
	}
	private static HSSFCellStyle getCellStyle(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColor.DARK_BLUE.index);
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		style.setFont(font);
		return style;
	}
	public static void main(String[] args)throws Exception {
		
	}
}
