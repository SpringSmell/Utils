package org.quick.utils.check

import android.content.Context
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

/**
 * Created by work on 2017/5/3.
 *银行卡检测
 * @author chris zou
 * @mail chrisSpringSmell@gmail.com
 */

object BankCardUtils {
    //该方法用于打开assets中的binNum文档资源，获得里面的binNum数据
    private fun openBinNum(context: Context): String? {
        val outputStream = ByteArrayOutputStream()
        var str: String? = null
        try {
            val inputStream = context.applicationContext.resources.assets.open("binNum.txt")
            val bytes = ByteArray(1024)
            var length = 0
            while (length != -1) {
                length = inputStream.read(bytes)
                outputStream.write(bytes, 0, length)
            }
            inputStream.close()
            outputStream.close()
            str = outputStream.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return str
    }

    //获得Bank card的前缀
    private fun getBinNum(context: Context): List<Long> {
        val binNum = openBinNum(context)
        val binArr = binNum!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val lon = ArrayList<Long>()
        for (i in binArr.indices) {
            if (i % 2 == 0)
                lon.add(java.lang.Long.parseLong(binArr[i]))

        }
        return lon
    }

    //获得BankName
    private fun getBinName(context: Context): List<String> {
        val binNum = openBinNum(context)
        val binArr = binNum!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val list = ArrayList<String>()
        for (i in binArr.indices) {
            if (i % 2 != 0) list.add(binArr[i])
        }
        return list
    }

    //    通过输入的卡号获得银行卡信息
    fun getNameOfBank(context: Context,binNum: Long): String {
        var index = 0
        index = binarySearch(getBinNum(context), binNum)
        return if (index == -1) {
            ""
        } else getBinName(context)[index]
    }

    //数量有上千条，利用二分查找算法来进行快速查找法
    fun binarySearch(srcArray: List<Long>, des: Long): Int {
        var low = 0
        var high = srcArray.size - 1
        while (low <= high) {
            val middle = (low + high) / 2
            when {
                des == srcArray[middle] -> return middle
                des < srcArray[middle] -> high = middle - 1
                else -> low = middle + 1
            }
        }
        return -1
    }
}
