package me.keatsyh.kaac.extended

import java.text.Format
import java.text.SimpleDateFormat
import java.util.*


//获取今天是星期几
fun Date.currentWeek(language: String = "chinese"): String {
    val weeks = when (language) {
        "english" -> arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        "chinese" -> arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        else -> arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    }
    val calendar = Calendar.getInstance()
    calendar.time = this
    var index: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
    if (index < 0) {
        index = 0
    }
    return weeks[index]
}

//获取当前日期
fun Date.today(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    val sdf: Format = SimpleDateFormat(format, Locale.getDefault())
    return sdf.format(this)
}


//获取上周的今天的日期
fun Calendar.lastWeekToday(format: String = "yyyy-MM-dd"): String {
    val sdf: Format = SimpleDateFormat(format, Locale.getDefault())
    val t: Long = this.timeInMillis
    val l: Long = t - 24 * 3600 * 1000 * 7
    return sdf.format(l)
}

//获取上个月今天的日期
fun Calendar.lastMonthToday(format: String = "yyyy-MM-dd"): String {
    val sdf: Format = SimpleDateFormat(format, Locale.getDefault())

    this.add(Calendar.MONTH, -1)
    return sdf.format(this.time)
}

//获取昨天的日期
fun Calendar.yesterday(format: String = "yyyy-MM-dd"): String? {
    val sdf: Format = SimpleDateFormat(format, Locale.getDefault())
    val t: Long = this.timeInMillis
    val l: Long = t - 24 * 3600 * 1000
    val d = Date(l)
    return sdf.format(d)
}

//获取上个月的第一天
fun Calendar.firstDayOfLastMonth(format: String = "yyyy-MM-dd"): String {
    val sdf: Format = SimpleDateFormat(format, Locale.getDefault())
    this.set(Calendar.DATE, 1)
    this.add(Calendar.MONTH, -1)
    return sdf.format(this.time)
}

//获取上个月的最后一天
fun Calendar.lastDayOfLastMonth(format: String = "yyyy-MM-dd"): String {
    val sdf: Format = SimpleDateFormat(format, Locale.getDefault())
    this.set(Calendar.DATE, 1)
    this.add(Calendar.MONTH, -1)
    this.roll(Calendar.DATE, -1)
    return sdf.format(this.time)
}

//判断是否是闰年
fun isLeapYear(year: Int): Boolean {
    if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
        return true
    }
    return false
}


// 字符串日期 获取想要格式的日期格式，栗子："2017—10-10 10:10:10"
fun time4String(time: String, inputFormat: String = "yyyy/MM/dd HH:mm:ss", outputFormat: String = "HH:mm"): String {
    //代转日期的字符串格式(输入的字符串格式)
    val inputsdf = SimpleDateFormat(inputFormat, Locale.getDefault())
    //获取想要的日期格式(输出的日期格式)
    val outputsdf = SimpleDateFormat(outputFormat, Locale.getDefault())
    val date: Date = inputsdf.parse(time)
    return outputsdf.format(date)
}

fun timestamp2String(timestamp: Long, format: String = "HH:mm"): String {
    val format = SimpleDateFormat(format, Locale.getDefault())
    return format.format(Date(timestamp))
}

fun dateInDateRang(currentDate: Date, beginDate: Date, endDate: Date): Boolean {
    if (currentDate.after(beginDate) && currentDate.before(endDate)) {
        return true
    }
    return false
}


// 判断两个日期大小  如，第一个日期大于第二个日期，返回true  反之false
fun isDateOneBigger(dateStr1: String, dateStr2: String, format: String = "yyyy/MM/dd"): Boolean {
    var isBigger = false
    //输入的格式，选择性更改
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    isBigger = sdf.parse(dateStr1).time >= sdf.parse(dateStr2).time
    return isBigger
}

//获取某个日期的前一天
fun dayBefore(specifiedDay: String, inputFormat: String = "yyyy/MM/dd", outputFormat: String = "yyyy/MM/dd"): String {
    //输出的日期格式
    val sdf = SimpleDateFormat(outputFormat, Locale.getDefault())
    //输入的String格式的日期
    val date: Date = SimpleDateFormat(inputFormat, Locale.getDefault()).parse(specifiedDay)
    val calendar = Calendar.getInstance()
    calendar.time = date
    val day = calendar.get(Calendar.DATE)
    calendar.set(Calendar.DATE, day - 1)
    return sdf.format(calendar.time)
}


//获取某个日期的后一天
fun dayAfter(specifiedDay: String, inputFormat: String = "yyyy/MM/dd", outputFormat: String = "yyyy/MM/dd"): String {
    //输出的日期格式
    val sdf = SimpleDateFormat(outputFormat, Locale.getDefault())
    //输入的String格式的日期
    val date: Date = SimpleDateFormat(inputFormat, Locale.getDefault()).parse(specifiedDay)
    val calendar = Calendar.getInstance()
    calendar.time = date
    val day = calendar.get(Calendar.DATE)
    calendar.set(Calendar.DATE, day + 1)
    return sdf.format(calendar.time)
}
