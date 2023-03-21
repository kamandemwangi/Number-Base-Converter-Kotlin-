package converter

import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import java.math.BigInteger
import kotlin.math.pow

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

            if (from != 10 && to != 10 ) {
                val baseTen = toBaseTen(b, from.toDouble())
                println("Conversion result: ${fromBaseTen(baseTen, to)}\n")
            } else if (to == 10)
                println("Conversion result: ${toBaseTen(b, from.toDouble())}\n")
            else
                println("Conversion result: ${fromBaseTen(b.toBigInteger(), to)}\n")
        }

    }
}
fun fromBaseTen(input: BigInteger, to: Int): String {
    val list = mutableListOf<BigInteger>()
    val zero = BigInteger.ZERO
    val divisor = to.toBigInteger()
    val nine = "9".toBigInteger()
    val diff = DIFF.toBigInteger()
    var copy = input
    var ans = ""
   while (copy > zero) {
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
    var index = 0.0
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
