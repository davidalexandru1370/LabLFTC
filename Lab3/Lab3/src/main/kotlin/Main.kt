import tests.SymbolTableTests
import kotlin.assert

fun main(args: Array<String>) {
   SymbolTableTests();
   val scanner: Scanner = Scanner("p3.txt")
   scanner.printPIF()
   scanner.printSymbolTable()
}


