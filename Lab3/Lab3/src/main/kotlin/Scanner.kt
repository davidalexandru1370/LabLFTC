import exceptions.ScannerException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.math.max

class Scanner {
    private var delimiters = listOf("{", "}", "(", ")", "[", "]", ":", ";", " ", "\t", ",", "\n", "'", "\"", "\r")
    private var operators = listOf("+", "-", "*", "/", "%", "=", "==", "!=", "<", "<=", ">", ">=", "&&", "||", "!")
    private var reservedWords =
        listOf(
            "read",
            "readInt",
            "if",
            "else",
            "display",
            "while",
            "def",
            "return",
            "const",
            "program",
            "begin",
            "end"
        )
    private var fileName: String = ""
    private lateinit var programInternalForm: ProgramInternalForm
    private val symbolTable: SymbolTable
    private lateinit var program: Program

    constructor(fileName: String) {
        this.fileName = fileName;
        this.symbolTable = SymbolTable();
        this.programInternalForm = ProgramInternalForm()
        scan()
    }


    private fun nextToken() {
        fun overcomeSpaces() {
            while (program.index < program.codeLength && program.code[program.index].toString().matches(Regex("[ \t\r\n]"))) {
                if (program.code[program.index] == '\n') {
                    program.lineIndex++
                }
                program.index++
            }
        }

        fun overcomeComments() {
            while (program.index < program.codeLength && program.code[program.index] == '/') {
                if (program.index + 1 < program.codeLength && program.code[program.index + 1] == '/') {
                    while (program.index < program.codeLength && program.code[program.index] != '\n') {
                        program.index++
                    }
                    program.lineIndex += 1
                }
            }
        }
        overcomeComments()
        overcomeSpaces()
    }

    private fun validateIdentifier(match: String): Boolean {
        if (reservedWords.contains(match) == false) {
            return false
        }

        if (!Regex("^(int|char|string)[ \t]+[a-zA-Z][a-zA-Z0-9]*").matches(match)) {
            return false
        }

        return symbolTable.hasIdentifier(match)
    }

    private fun parseIdentifier(): Boolean {
        var identifierRegex: Regex = Regex("(int|string|char)\\s+[a-zA-Z][a-zA-Z0-9]*")
        var substr = program.code.substring(program.index)
        println(substr.matches(identifierRegex))
        var match = identifierRegex.find(program.code, program.index)

        if (match == null) {
            return false
        }

        

        program.index += match.value.length

        return true
    }

    private fun parseSymbol(): Boolean {
        val possibleSymbol = program.code
            .substring(program.index)
            .split(Regex("[ \t\r\n]", RegexOption.IGNORE_CASE))
            .filter { it.isNotEmpty() }
            .first()

        val allSymbols = delimiters.union(operators);
        if ((possibleSymbol in allSymbols) == false) {
            return false
        }
        if (possibleSymbol == "\n") {
            program.lineIndex += 1
        }
        program.index += possibleSymbol.length
        return true
    }

    private fun parseReservedWord(): Boolean {
        var possibleToken = program.code
            .substring(program.index)
            .split(Regex("[ \t\r\n]", RegexOption.IGNORE_CASE))
            .filter { it.isNotEmpty() }
            .first()

        var hasMatch: Boolean = false
        var length: Int = 0
        for (token in reservedWords) {
            if (possibleToken.startsWith(token)) {
                hasMatch = true
                length = max(length, token.length)
            }
        }

        if (hasMatch == false) {
            return false
        }

        program.index += length + 1
        return true
    }

    private fun parseToken() {
        nextToken()
        if (program.index >= program.codeLength) {
            return
        }

        if (parseIdentifier()) {
            return
        }
        if (parseReservedWord()) {
            return
        }
        if (parseSymbol()) {
            return
        }

        throw ScannerException("Invalid token at line ${program.lineIndex} at index ${program.index}")

    }

    fun scan(): ProgramInternalForm {
        val file: Path = Path("src/main/kotlin/input/" + fileName)
        this.program = Program(Files.readString(file))
        while (program.index < program.codeLength) {
            parseToken()
        }

        return this.programInternalForm
    }

}
