package com.oo.vul.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class LogError {

	public static String getDetailError(Exception e) {
		String result = e.toString();
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			result = sw.toString();
			sw.close();
			pw.close();
		} catch (IOException e1) {
		}
		return result;
	}
}
