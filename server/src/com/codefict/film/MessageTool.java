package com.codefict.film;

import org.apache.commons.lang.StringEscapeUtils;

public class MessageTool {
	public static String ConvertToSQLSafeString(String UnsafeStr) {
		return StringEscapeUtils.escapeSql(UnsafeStr);
	}
}
