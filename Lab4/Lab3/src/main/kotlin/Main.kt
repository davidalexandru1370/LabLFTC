import domain.FiniteAutomata
import tests.SymbolTableTests
import kotlin.assert

fun main(args: Array<String>) {
    SymbolTableTests();
    val menuOptions = mutableMapOf(
        1 to "1. Print integer automata",
        2 to "2. Print string automata",
    )
    val scanner: Scanner = Scanner("p3.txt")
    scanner.printPIF()
    scanner.printSymbolTable()

    while (true) {
        println("Choose an option:")
        menuOptions.forEach { (key, value) -> println(value) }
        val option = readLine()!!.toInt()
        when (option) {
            1 -> {
                println(FiniteAutomata("src/main/kotlin/input/fa_integers.txt").toString())
            }
            2 -> {
                println(FiniteAutomata("src/main/kotlin/input/fa_identifiers.txt").toString())
            }
            else -> {
                println("Invalid option")
            }
        }
    }
}


