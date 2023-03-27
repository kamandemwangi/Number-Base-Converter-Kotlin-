package converter

import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.math.pow
import kotlin.text.StringBuilder

val BASE_10: BigInteger = BigInteger.TEN
val ZERO: BigInteger = BigInteger.ZERO
val FLOATING_POINT_ZERO: BigDecimal = BigDecimal.ZERO
const val DIFF = 55

fun main() {
    init()
}

fun init() {
    while (true) {
        println("Enter two numbers in format: {source base} {target base} (To quit type /exit)")
        val a = readln()
        if (a == "/exit") break
        val (from, to) = a.split(" ").map { it.toInt() }

        while (true) {
            println("Enter number in base $from to convert to base $to (To go back type /back) ")
            val b = readln()
            if (b == "/back")
                break

            if (b.hasFloatingPoint()) {
                if (from != 10 && to != 10) {
                    val baseTen = toBaseTenWthFloatingPoint(b, from.toDouble())
                    println("Conversion result: ${fromBaseTenWithFloatingPoint(baseTen.toString(), to.toBigDecimal())}\n")
                } else if (to == 10)
                    println("Conversion result: ${toBaseTenWthFloatingPoint(b, from.toDouble()).setScale(5, RoundingMode.HALF_EVEN)}\n")
                else
                    println("Conversion result: ${fromBaseTenWithFloatingPoint(b, to.toBigDecimal())}\n")
            } else {
                if (from != 10 && to != 10) {
                    val baseTen = toBaseTen(b, from.toDouble())
                    println("Conversion result: ${fromBaseTen(baseTen, to)}\n")
                } else if (to == 10)
                    println("Conversion result: ${toBaseTen(b, from.toDouble())}\n")
                else
                    println("Conversion result: ${fromBaseTen(b.toBigInteger(), to)}\n")
            }

        }

    }
}

fun fromBaseTenWithFloatingPoint(input: String, to: BigDecimal): String {
    val ans = StringBuilder()
    val f = StringBuilder()
    val set = HashSet<BigDecimal>()
    val decimal = input.toBigDecimal()
    val left = decimal.setScale(0, RoundingMode.FLOOR)
    var right = decimal - left
    ans.append(fromBaseTen(left.toBigInteger(), to.toInt()))
    ans.append(".")
    if (right.toString() == "0" || right.toString().startsWith("0E-")) {
        ans.append("00000")
        return ans.toString().lowercase()
    }
    while (right > FLOATING_POINT_ZERO) {
        val d = right * to
        val l = d.setScale(0, RoundingMode.FLOOR)
        f.append(fromBaseTen(l.toBigInteger(), to.toInt()))
        if (f.length == 5)
            break
        right = d - l
        if (!set.add(right))
            break
    }
    ans.append(f)
    if (f.length < 5) {
        repeat(5 - f.length) {
            ans.append("0")
        }
    }
    return ans.toString().lowercase()
}

fun fromBaseTen(input: BigInteger, to: Int): String {
    if (input == ZERO) return "0"
    val list = mutableListOf<BigInteger>()
    val divisor = to.toBigInteger()
    val nine = "9".toBigInteger()
    val diff = DIFF.toBigInteger()
    var copy = input
    var ans = ""
    while (copy > ZERO) {
        val remainder = copy % divisor
        list.add(remainder)
        copy /= divisor
    }

    for (i in list.size - 1 downTo 0) {
        if (list[i] > nine) {
            val a = list[i] + diff
            ans += a.toInt().toChar()
        } else
            ans += list[i]
    }
    return ans
}

fun toBaseTen(input: String, from: Double): BigInteger {
    var index = 0
    var ans = "0".toBigDecimal()
    for (i in input.length - 1 downTo 0) {
        val curr = input[i]
        ans += if (!curr.isInt()) {
            val num = curr.uppercaseChar().code - DIFF
            (from.pow(index) * num).toBigDecimal()
        } else
            (from.pow(index) * curr.digitToInt()).toBigDecimal()
        index++
    }
    return ans.toBigInteger()
}

fun toBaseTenWthFloatingPoint(input: String, from: Double): BigDecimal {
    var i = 1
    var ans = BigDecimal("0")
    val left = input.substringBefore(".")
    val right = input.substringAfter(".")

    ans += toBaseTen(left, from).toBigDecimal()

    for (el in right) {
        ans += if (el.isInt()) {
            BigDecimal(el.toString()).divide(from.pow(i).toBigDecimal(),
                9, RoundingMode.HALF_EVEN)
        } else {
            BigDecimal(el.uppercaseChar().code - DIFF).divide(from.pow(i).toBigDecimal(),
                9, RoundingMode.HALF_EVEN)
        }
        i++
    }
    return ans
}

fun addSeparator(num: String): String {
    val stringBuilder = StringBuilder()
    return if (num.hasFloatingPoint()) {
        val whole = num.toBigDecimal()
        val left = whole.setScale(0, RoundingMode.FLOOR)
        val right = whole - left
        val r = right.toString().removePrefix("0")

        stringBuilder.append(addCommaSeparator(left.toBigInteger()))
        stringBuilder.append((r))
        stringBuilder.toString()


    } else
        addCommaSeparator(num.toBigInteger())
}

fun addCommaSeparator(num: BigInteger): String {
    var copy = num
    var count = 0
    val ans = StringBuilder()
    while (copy > ZERO) {
        ans.append(copy % BASE_10)
        count++
        if (count == 3 && copy >= BASE_10) {
            ans.append(",")
            count = 0
        }
        copy /= BASE_10
    }
    return ans.reverse().toString()
}

fun Char.isInt(): Boolean {
    return try {
        this.digitToInt()
        true
    } catch (e: IllegalArgumentException) {
        false
    } catch (e: NumberFormatException) {
        false
    }
}
fun String.hasFloatingPoint(): Boolean {
    for (ch in this) {
        if (ch == '.')
            return true
    }
    return false
}
