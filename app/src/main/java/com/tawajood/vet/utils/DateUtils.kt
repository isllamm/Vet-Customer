package com.tawajood.snail.utils

import android.annotation.SuppressLint
import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun getDayMonthFormatDate(date: String): String {
    val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    val targetFormat: DateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    val mDate: Date = originalFormat.parse(date)!!
    return targetFormat.format(mDate)
}

fun getDayMonthYearFormatDate(date: String): String {
    val originalFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    val targetFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val mDate: Date = originalFormat.parse(date)!!
    return targetFormat.format(mDate)
}

@SuppressLint("SimpleDateFormat")
fun getHoursMinutesFromDate(time: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val dateObj = sdf.parse(time)
    return SimpleDateFormat("hh:mm a").format(dateObj!!)
}

fun getCustomDate(
    originalPattern: String,
    targetPattern: String,
    date: String,
    locale: Locale = Locale.ENGLISH
): String {
    val originalFormat: DateFormat = SimpleDateFormat(originalPattern, Locale.ENGLISH)
    val targetFormat: DateFormat = SimpleDateFormat(targetPattern, locale)
    val mDate: Date = originalFormat.parse(date)!!
    return targetFormat.format(mDate)
}

fun isBeforeToday(date: String, pattern: String): Boolean{
    var enteredDate: Date? = null
    try {
        val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)
        enteredDate = sdf.parse(date)
    } catch (ex: Exception) {
        Log.d("7imaZz", "isBefore: ${ex.message}")
    }
    val currentDate = Date()
    if (enteredDate!!.after(currentDate)) {
        return false
    }
    return true
}
