package converter

import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import kotlin.math.pow
const val BASE_16 = 16
const val BASE_10 = 10
const val BASE_8 = 8.0
const val BASE_2 = 2.0

enum class HEX(val num: Int) {
    A(10),
    B(11),
    C(12),
    D(13),
    E(14),
    F(15)
}

// Do not delete this line

fun main() {
   init()
}

fun init() {
    while (true) {
        println("Do you want to convert /from decimal or /to decimal? (To quit type /exit)")
        when (readln()) {
            "/from" -> {
                fromDecimal()
            }

            "/to" -> {
                toDecimal()
            }

            "/exit" -> break
        }
    }


}

fun fromDecimal() {
    print("Enter number in decimal system: ")
    val decimal = readln().toInt()
    print("Enter target base: ")

    when (readln().toInt()) {
        BASE_2.toInt() -> {
            println("Conversion result: ${decimalToBinary(decimal)}\n")
        }
        BASE_8.toInt() -> {
            println("Conversion result: ${decimalToOctal(decimal)}\n")
        }
        BASE_16 -> {
            println("Conversion result: ${decimalToHex(decimal)}\n")
        }
    }
}

fun toDecimal() {
    print("Enter source number: ")
    val sourceNumber = readln()
    print("Enter source base: ")
    when (readln().toInt()) {
        BASE_16 -> {
            println("Conversion to decimal result: ${hexToDecimal(sourceNumber.uppercase())}\n")
        }
        BASE_8.toInt() -> {
            println("Conversion to decimal result: ${octalToDecimal(sourceNumber.toInt())}\n")

        }
        BASE_2.toInt() -> {
            println("Conversion to decimal result: ${binaryToDecimal(sourceNumber)}\n")
        }
    }

}

fun decimalToBinary(num: Int): String {
    var q = num
    var out = ""
    while (q > 0) {
        out += q % 2
        q /= 2
    }

    return out.reversed()
}

fun decimalToOctal(num: Int): Int {
    var out = ""
    var q = num
    while (q > 0) {
        out += q % 8
        q /= 8
    }
    return out.reversed().toInt()
}

fun octalToDecimal(num: Int): Int {
    var out = 0.0
    var q = num
    var p = 0
    while (q > 0) {
        val r = q % BASE_10
        out += r * BASE_8.pow(p++)
        q /= BASE_10
    }
    return out.toInt()
}

fun binaryToDecimal(num: String): Int {
    val reversed = num.reversed()
    var out = 0.0
    var p = 0
    for (bit in reversed) {
        out += bit.digitToInt() * BASE_2.pow(p++)
    }
    return out.toInt()
}

fun decimalToHex(num: Int): String {
    var out = ""
    val binary = decimalToBinary(num)
    var chunked = ""
    var i = 1

    for (j in binary.length - 1 downTo 0) {
        chunked += binary[j]
        if (i++ == 4 || (chunked.isNotEmpty() && j == 0)) {
            val r = chunked.reversed()
            val d = binaryToDecimal(r)
            if (d > 9) {
                for (v in HEX.values()) {
                    if (v.num == d) out+= v.name
                }
            } else out += d
            chunked = ""
            i = 1
        }
    }
    return out.reversed()
}

fun hexToDecimal(input: String): Int {
    var binary = ""
    for (i in input) {
        if (!i.isInt()) {
            for (j in HEX.values()) {
                if (j.name == i.toString()) {
                    binary += decimalToBinary(j.num)
                }
            }
        } else{
            val res = decimalToBinary(i.digitToInt())
            if (res.length < 4) {
                repeat (4 - res.length) {
                    binary += "0"
                }
                binary += res
            } else binary += res
        }
    }
    return binaryToDecimal(binary)
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
