import java.io.BufferedReader
import java.io.File

class Grammar(filePath: String) {
    private val productions: HashMap<String, List<String>> = HashMap()
    private val terminals: MutableList<String> = mutableListOf()
    private val nonTerminals: MutableList<String> = mutableListOf()
    private var startSymbol: String = ""

    init {
        readFromFile(filePath)
    }

    private fun readFromFile(filePath: String) {
        val file = File(filePath)
        val bufferedReader: BufferedReader = file.bufferedReader()
        val inputString = bufferedReader.use { it.readText() }
    }

}