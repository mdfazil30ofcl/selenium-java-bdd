package tests;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class test {
	public static Connection conn = null;

	public static void main(String args[]) throws SQLException, IOException, ParseException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("A", "Apple");
		map.put("B", "Bird");
		map.put("C", "Car");
		map.put("D", "Duck");
		map.put("E", "Elephant");
		System.out.println(map);
		System.out.println(map.keySet().size());
		Set<String> set = new HashSet<String>();
		set = map.keySet();
		for(String s : set) {
			System.out.println(s);
		}
	}
	
	public static void compareMinuteDiffWithRange(String time1, String time2, String format, int withinRange, String msg) {
		DateTimeFormatter formatDate = DateTimeFormat.forPattern(format);
		DateTime dt1 = DateTime.parse(time1, formatDate);
		DateTime dt2 = DateTime.parse(time2, formatDate);
		if (Math.abs(dt2.getMillis() - dt1.getMillis()) == (withinRange * 60000))
			System.out.println(msg + ": Time difference matches approximately between " + time1 + " and " + time2
					+ " having the range within " + withinRange + " minute(s)");
		else
			System.out.println(msg + ": Time difference not matches between " + time1 + " and " + time2
					+ " having the range within " + withinRange + " minute(s)");
	}

	public static boolean compareNumberWithRange(int no1, int no2, int rangeValue) {
		System.out.println("Checking values for no1 = " + no1 + " and no2 = " + no2 + " with range = " + rangeValue);
		for (int i = no1; i <= no1 + rangeValue; i++) {
			System.out.println("inside for++ loop : " + i);
			if (no2 == i) {
				System.out.println("inside if loop : " + i);
				return true;
			}
		}
		for (int i = no1; i >= no1 - rangeValue; i--) {
			System.out.println("inside for-- loop : " + i);
			if (no2 == i) {
				System.out.println("inside if loop : " + i);
				return true;
			}
		}
		return false;
	}

	public static String getStringBetweenChar(String str, String st, String en) {
		return str.replaceAll(".*\\" + st + "|\\" + en + ".*", "");
	}

	public static String getBetweenStrings(String text, String textFrom, String textTo) {
		String result = "";
		result = text.substring(text.indexOf(textFrom) + textFrom.length(), text.length());
		result = result.substring(0, result.indexOf(textTo));
		return result;
	}

	public static String getStringBetweenChar(String str, char st, char en) {
		return str.replaceAll(".*\\" + st + "|\\" + en + ".*", "");
	}

	public static String getDateInFormat(String date, String dateFormat, String formatToBeReturned)
			throws ParseException {
		DateFormat dFormat1 = new SimpleDateFormat(dateFormat);
		DateFormat dFormat2 = new SimpleDateFormat(formatToBeReturned);
		Date dateToReturn = dFormat1.parse(date);
		return dFormat2.format(dateToReturn);
	}

}