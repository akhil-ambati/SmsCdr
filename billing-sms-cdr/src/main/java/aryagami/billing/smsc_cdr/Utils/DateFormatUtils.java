package aryagami.billing.smsc_cdr.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class DateFormatUtils {

	public static Date convertStringToDate(String givenDate, String formatType, Integer correctionDates) {

		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;

		try {
			date = formatter.parse(givenDate);
			if (correctionDates > 0)
				date = DateUtils.addDays(date, correctionDates);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}
		return date;
	}

	public static Date convertDateToDateWithFormat(Date givenDate, String formatType, Integer correctionDates) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;

		try {
			date = formatter.parse(formatter.format(givenDate));
			if (correctionDates != 0) {
				date = DateUtils.addDays(date, correctionDates);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block

		}
		return date;
	}
	
	public static String convertDateToStringWithFormat(Date givenDate, String formatType, Integer correctionDates) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;

		try {
			date = formatter.parse(formatter.format(givenDate));
			if (correctionDates != 0) {
				date = DateUtils.addDays(date, correctionDates);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block

		}
		String actualDate = formatter.format(date);
		return actualDate;
	}

	
}
