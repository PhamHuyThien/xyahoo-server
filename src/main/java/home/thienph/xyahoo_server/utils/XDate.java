package home.thienph.xyahoo_server.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Tiện ích xử lý Date trong Java
 */
public class XDate {

    public static final String DEFAULT_FORMAT = "dd-MM-yyyy HH:mm:ss";
    public static final String HHmmddMM = "HH:mm dd-MM";

    /**
     * Format Date -> String
     *
     * @param date Date cần format
     * @return String date với định dạng mặc định yyyy-MM-dd HH:mm:ss
     */
    public static String format(Date date) {
        return format(date, DEFAULT_FORMAT);
    }

    /**
     * Format Date -> String với format custom
     *
     * @param date    Date cần format
     * @param pattern định dạng ví dụ: "dd/MM/yyyy"
     * @return chuỗi date
     */
    public static String format(Date date, String pattern) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * Parse String -> Date
     *
     * @param dateStr chuỗi date
     * @param pattern định dạng của chuỗi
     * @return Date
     * @throws ParseException nếu format không khớp
     */
    public static Date parse(String dateStr, String pattern) throws ParseException {
        if (dateStr == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(dateStr);
    }
}
