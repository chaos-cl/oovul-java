package com.oo.vul;
import java.io.InputStream;
import java.util.Properties;

public class FilePath {

	public static String EXCEL_IMPORT_FILE_PATH="";
	
	public static String EXCEL_EXPORT_FILE_PATH="";
	
	public static String TXT_FILEPATH="";
	
	static{
		try {
			InputStream input = FilePath.class.getResourceAsStream("/conf.properties");
			Properties pro=new Properties();
			pro.load(input);
			EXCEL_IMPORT_FILE_PATH = pro.getProperty("excel.import.filepath");
			TXT_FILEPATH=pro.getProperty("txt.filepath");
			EXCEL_EXPORT_FILE_PATH=pro.getProperty("excel.export.filepath");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		System.out.println("CVE-1015-4863".matches("CVE\\-\\d{4}\\-.+"));
	}
}
