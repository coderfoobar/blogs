

# Java 获取两个时间点之间的所有月份

```Java
private static List<String> getMonthBetween(String minDate, String maxDate) throws ParseException {
    ArrayList<String> result = new ArrayList<String>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

    Calendar min = Calendar.getInstance();
    Calendar max = Calendar.getInstance();

    min.setTime(sdf.parse(minDate));
    min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

    max.setTime(sdf.parse(maxDate));
    max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

    Calendar curr = min;
    while (curr.before(max)) {
        result.add(sdf.format(curr.getTime()));
        curr.add(Calendar.MONTH, 1);
    }

    return result;
}
```