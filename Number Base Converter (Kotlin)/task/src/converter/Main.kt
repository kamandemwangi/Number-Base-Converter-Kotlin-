package converter

import kotlin.math.pow
const val BASE_16 = 16
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
    println("Enter number in decimal system:")
    val decimal = readln().toInt()
    println("Enter target base:")

    when (readln().toInt()) {
        BASE_2.toInt() -> {
            println("Conversion result:")
            println(decimalToBinary(decimal))
        }
        BASE_8.toInt() -> {
            println("Conversion result:")
            println(decimalToOctal(decimal))
        }
        BASE_16 -> {
            println("Conversion result:")
            println(decimalToHex(decimal))
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
