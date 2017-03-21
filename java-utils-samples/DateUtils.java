


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



    /**
     * 根据给定的开始时间和结束时间来获取两个时间点之间的月份（包含两个时间点所在的月份）
     * @param begin  开始时间
     * @param end  结束时间
     * @param pattern  返回月份的格式 例如 yyyyMM 或 yyyy-MM 或 yyyy/MM
     * @return
     * @throws ParseException
     */
    public static List<String> getMonthsBetween(Date begin , Date end , String pattern) throws ParseException {

        ArrayList<String> months = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(sdf.format(begin)));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(sdf.format(end)));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar cursor = min;
        while (cursor.before(max)) {
            months.add(sdf.format(cursor.getTime()));
            cursor.add(Calendar.MONTH, 1);
        }

        return months;
    }

    /**
     * 根据给定的开始时间和结束时间来获取两个时间点之间的月份（包含两个时间点所在的月份）
     * @param begin 开始时间
     * @param end   结束时间
     * @param originPattern   开始时间月结束时间本身的格式
     * @param targetPattern   返回月份的格式 例如 yyyyMM 或 yyyy-MM 或 yyyy/MM
     * @return
     * @throws ParseException
     */
    public static List<String> getMonthsBetween(String begin ,String end , String originPattern , String targetPattern) throws ParseException{

        SimpleDateFormat sdf = new SimpleDateFormat(originPattern);

        Date beginTime = sdf.parse(begin);
        Date endTime   = sdf.parse(end);

        return getMonthsBetween(beginTime,endTime,targetPattern);
    }


    /**
     * 获取给定日期的零点
     * @param date
     * @return
     */
    public static Date getStartTimeOfDate (Date date) {
        Calendar day = Calendar.getInstance();

        day.setTime(date);
        day.set(Calendar.HOUR_OF_DAY , 0);
        day.set(Calendar.MINUTE , 0);
        day.set(Calendar.SECOND , 0);
        day.set(Calendar.MILLISECOND , 0);

        return day.getTime();
    }

    /**
     * 获取个定日期的未点
     * @param date
     * @return
     */
    public static Date getEndTimeOfDate (Date date){
        Calendar day = Calendar.getInstance();

        day.setTime(date);
        day.set(Calendar.HOUR_OF_DAY , 23);
        day.set(Calendar.MINUTE , 59);
        day.set(Calendar.SECOND , 59);
        day.set(Calendar.MILLISECOND , 999);

        return day.getTime();
    }


    /**
     * 获取给定日期的零点
     * @param originDate  给定的日期
     * @param originPattern 给定日期的格式
     * @return
     * @throws ParseException
     */
    public static Date getStartTimeOfDate (String originDate,String originPattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(originPattern);

        Calendar day = Calendar.getInstance();

        day.setTime(sdf.parse(originDate));
        day.set(Calendar.HOUR_OF_DAY , 0);
        day.set(Calendar.MINUTE , 0);
        day.set(Calendar.SECOND , 0);
        day.set(Calendar.MILLISECOND , 0);

        return day.getTime();
    }


    /**
     * 获取给定日期的未点
     * @param originDate 给定日期
     * @param originPattern 给定日期的格式
     * @return
     * @throws ParseException
     */
    public static Date getEndTimeOfDate (String originDate , String originPattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(originPattern);
        Calendar day = Calendar.getInstance();

        day.setTime(sdf.parse(originDate));
        day.set(Calendar.HOUR_OF_DAY , 23);
        day.set(Calendar.MINUTE , 59);
        day.set(Calendar.SECOND , 59);
        day.set(Calendar.MILLISECOND , 999);

        return day.getTime();
    }

}