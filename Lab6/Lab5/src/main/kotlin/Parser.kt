import java.util.Objects

class Parser(private var grammar: Grammar) {
    private val EPSILON: String = "epsilon"
    private val NULL: String = "null"
    private val firstTable: HashMap<String, HashSet<String>> = HashMap()
    private val followTable: HashMap<String, HashSet<String>> = HashMap()

    init {
        first()
    }

    private fun first() {
        for (nonTerminal in grammar.nonTerminals) {
            val f0 = getF0(nonTerminal)
            firstTable[nonTerminal] = f0
        }

        var noMoreChanges: Boolean = true

        do {
            var nextColumn: HashMap<String, HashSet<String>> = HashMap()
            for (nonTerminal in grammar.nonTerminals) {
                val productionsForNonTerminal: HashSet<ArrayList<String>> =
                    grammar.getProductionsForGivenNonTerminalList(nonTerminal)
                val previousValues: HashSet<String> = firstTable[nonTerminal]!!

                for (production in productionsForNonTerminal) {
                    val rightNonTerminals: ArrayList<String> = ArrayList()
                    var rightTerminal: String = ""
                    for (symbol in production) {
                        if (grammar.nonTerminals.contains(symbol)) {
                            rightNonTerminals.add(symbol)
                        } else {
                            rightTerminal = symbol
                            break
                        }
                    }
                    val concatenationResult: ArrayList<String> = concatenation(rightTerminal, rightNonTerminals)
                    previousValues.addAll(concatenationResult)
                }

                nextColumn[nonTerminal] = previousValues
            }
            if (nextColumn.entries.containsAll(firstTable.entries)) {
                noMoreChanges = false
            }

        } while (noMoreChanges)

        firstTable.forEach { (k, v) -> println("${k} -> ${v}") }
    }

    private fun concatenation(rightTerminal: String, rightNonTerminals: ArrayList<String>): ArrayList<String> {
        if (rightNonTerminals.size == 0) {
            arrayListOf<String>()
        }
        if (rightNonTerminals.size == 1) {
            firstTable[rightNonTerminals[0]]!!
        }


        val concatenationResult: ArrayList<String> = ArrayList()
        var allEpsilon: Boolean = true

        for (nonTerminal in rightNonTerminals) {
            if (!firstTable[nonTerminal]!!.contains(EPSILON)) {
                allEpsilon = false
            }
        }

        if (allEpsilon) {
            concatenationResult.add(if (rightTerminal == "") EPSILON else rightTerminal)
        }

        for (index in 0..<rightNonTerminals.size) {
            var containsEpsilon: Boolean = false
            val productions: HashSet<String> = firstTable[rightNonTerminals[index]]!!
            for (production in productions) {
                if (production == EPSILON) {
                    containsEpsilon = true
                } else {
                    concatenationResult.add(production)
                }
            }
            if (!containsEpsilon) {
                break
            }
        }

        return concatenationResult
    }

    private fun getF0(nonTerminal: String): HashSet<String> {
        val f0: HashSet<String> = HashSet()
        for (production in grammar.productions) {
            if (production.key == nonTerminal) {
                for (productionList in production.value) {
                    var firstSymbol: String = productionList.iterator().next()
                    if (grammar.terminals.contains(firstSymbol) || firstSymbol == this.EPSILON) {
                        f0.add(productionList.iterator().next())
                    }
                }
            }
        }
        return f0

    }
}