package servlets;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {
	public static Date getDate(Object dateString) {
		String dataFormularString = (String)(dateString);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		try {
			date = sdf1.parse(dataFormularString);

		} catch (java.text.ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		date = c.getTime();
		
		System.out.println(date.toString());
		return new java.sql.Date(date.getTime());
	}
}
