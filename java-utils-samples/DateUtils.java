


/******************************************
 *
 * 功能描述:
 *
 *  1. 从字符串转为日期时间
 *  2. 从日期时间转为字符串
 *  3. 比较日期时间,字符串
 *  4. 计算日期，字符串
 *  5. 字符串日期时间从一种格式转为另一种格式
 *
 ******************************************/

public class DateUtils {


    private static final String DEFAULT_DATE_FORMAT         = "yyyy-MM-dd";
    private static final String DEFAULT_DATE_TIME_24FORMAT  = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_TIME_12FORMAT  = "yyyy-MM-dd hh:mm:ss";
    private static final String DEFAULT_TIMESTAMP_FORMAT    = "yyyy-MM-dd HH:mm:ssSSS";

    public static String dateFmt;
    public static String dateTime24Fmt;
    public static String dateTime12Fmt;
    public static String timestampFmt;

    public Date getNow(){
        return new Date();
    }



    public Date getDate(String date,String fmt){
        SimpleDateFormat sdf = new SimpleDateFormat("fmt");
        sdf.parse();
    }

}