package org.quick.utils

import android.content.Context
import android.graphics.Point
import android.text.TextUtils
import org.quick.utils.check.CheckUtils
import org.quick.utils.check.IDCardUtils
import org.quick.utils.check.MobileCheckUtils
import java.text.DecimalFormat
import java.util.regex.Pattern

/**
 * Created by Administrator on 2016/5/4.
 */
object FormatUtils {

    fun dip2px(context: Context, dpValue: Float): Float = dpValue * context.applicationContext.resources.displayMetrics.density + 0.5f


    fun px2dip(context: Context, pxValue: Float): Float = pxValue / context.applicationContext.resources.displayMetrics.density + 0.5F

    fun double(value: Double, rules: String): String {
        val df = DecimalFormat(rules)
        return df.format(value)
    }

    fun double(value: Double): String = DecimalFormat("#,###.00").format(value)

    /**
     * 修剪浮点类型
     *
     * @param value value
     * @param rules 规则(如:0.00保留2位小数)
     * @return string or "" or value
     */
    fun double(value: String?, rules: String?): String {
        if (value == null || value.isEmpty() || rules == null || rules.isEmpty()) {
            return ""
        }
        return try {
            double(java.lang.Double.parseDouble(value), rules)
        } catch (e: Exception) {
            value
        }

    }

    /**
     * 获取两点间距离,单位：px
     *
     * @param x1 第一个点
     * @param x2 第二个点
     * @return
     * @formula |AB| = sqrt((X1-X2)^2 + (Y1-Y2)^2)
     */
    fun distance(x1: Point, x2: Point): Double = distance(x1.x.toFloat(), x2.x.toFloat(), x1.y.toFloat(), x2.y.toFloat())


    fun distance(x1: Float, x2: Float, y1: Float, y2: Float): Double {
        val x = Math.abs(x2 - x1)
        val y = Math.abs(y2 - y1)
        return Math.sqrt((x * x + y * y).toDouble())
    }

    fun numberSplit(number: Double): String {
        return numberSplit(number, ",", 3, 2)
    }

    fun numberSplit(number: Double, endLength: Int): String {
        return numberSplit(number, ",", 3, endLength)
    }

    /**
     * 格式化数字，用逗号分割
     *
     * @param number      1000000.7569 to 1,000,000.76 or
     * @param splitChar   分割符号
     * @param endLength   保存多少小数位
     * @param splitLength 分割位数
     * @return 格式化完成的字符串
     */
    fun numberSplit(number: Double, splitChar: String, splitLength: Int, endLength: Int): String {

        var tempPattern: String
        var tempSplitStr = ""

        for (index in 0 until splitLength) tempSplitStr += "#"

        tempPattern = tempSplitStr + splitChar + tempSplitStr.substring(0, splitLength - 1) + "0." /*###,##0.*/

        for (index in 0 until endLength) tempPattern += "0" /*###,##0.00*/

        return DecimalFormat(tempPattern).format(number)
    }

    /**
     * 格式化手机号码，用空格分割
     *
     * @param mobileNumber 12345678910 to 123 4567 8910
     * @return
     */
    fun mobileSpace(mobileNumber: String): String? {
        return if (TextUtils.isEmpty(mobileNumber) || mobileNumber.length < 11) {
            mobileNumber
        } else mobileNumber.substring(0, 3) + " " + mobileNumber.substring(3, 11 - 4) + " " + mobileNumber.substring(11 - 4, mobileNumber.length)
    }

    /**
     * 格式化手机号码，用空格分割
     *
     * @param mobileNumber 12345678910 to 123****8910
     * @return
     */
    fun mobileStar(mobileNumber: String): String? {
        return if (TextUtils.isEmpty(mobileNumber) || mobileNumber.length < 11 || !MobileCheckUtils.isMobileNo(mobileNumber)) {
            mobileNumber
        } else mobileNumber.substring(0, 3) + "****" + mobileNumber.substring(11 - 4, mobileNumber.length)
    }

    /**
     * @param idCardNumber 5000909342893289489328 to 5**************************8
     * @return
     */
    fun formatIdCardStar(idCardNumber: String): String? {
        return if (TextUtils.isEmpty(idCardNumber) || !IDCardUtils.IDCardValidate(idCardNumber).equals(IDCardUtils.SUCCESS_INFO, ignoreCase = true)) {
            idCardNumber
        } else idCardNumber.substring(0, 1) + "****************" + idCardNumber.substring(17, 18)
    }

    /**
     * @param email zoidfkdfdsl@gmail.com to z**********@gmail.com
     * @return
     */
    fun emailStar(email: String): String? {
        return if (TextUtils.isEmpty(email) || !CheckUtils.isEmail(email)) {
            email
        } else email.substring(0, 1) + "*******" + email.substring(email.lastIndexOf("@") - 1, email.length)
    }

    /**
     * @param bandCardNumber
     * @return
     */
    fun bankCard(bandCardNumber: String): String? {
        return if (TextUtils.isEmpty(bandCardNumber)) {
            bandCardNumber
        } else "**** **** **** " + bandCardNumber.substring(bandCardNumber.length - 4, bandCardNumber.length)
    }

    /**
     * 删除html标签内容
     *
     * @param htmlStr
     * @return
     */
    fun htmlTagToDel(htmlStr: String): String {
        var htmlStr = htmlStr
        val regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>" //定义script的正则表达式
        val regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>" //定义style的正则表达式
        val regEx_html = "<[^>]+>" //定义HTML标签的正则表达式

        val p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE)
        val m_script = p_script.matcher(htmlStr)
        htmlStr = m_script.replaceAll("") //过滤script标签

        val p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE)
        val m_style = p_style.matcher(htmlStr)
        htmlStr = m_style.replaceAll("") //过滤style标签

        val p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE)
        val m_html = p_html.matcher(htmlStr)
        htmlStr = m_html.replaceAll("") //过滤html标签

        return htmlStr.trim { it <= ' ' } //返回文本字符串
    }

    /**
     * 格式化流量
     *
     * @param bytes 字节
     * @return
     */
    fun flow(bytes: Double): String {
        val gb = bytes / 8f / 1024f / 1024f / 1024f
        val mb = bytes / 8f / 1024f / 1024f
        val kb = bytes / 8f / 1024f
        val bit = bytes / 8
        return if (gb > 1) {//GB
            String.format("%sGB", numberSplit(gb))
        } else if (mb > 1) {
            String.format("%sMB", numberSplit(mb))
        } else if (kb > 1) {
            String.format("%sKB", numberSplit(kb))
        } else
            String.format("%sByte", numberSplit(bit))
    }
}
