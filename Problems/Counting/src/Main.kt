import java.util.*

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val numbers = mutableListOf<Int>()

    repeat(scanner.nextInt()) {
        numbers.add(scanner.nextInt())
    }
    
    val toFind = scanner.nextInt()
    println(numbers.count { it == toFind })
}