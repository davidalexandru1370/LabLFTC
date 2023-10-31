import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.io.path.Path

class Scanner {
    private var delimiters = listOf("{", "}", "(", ")", "[", "]", ":", ";", " ", "\t", ",", "\n", "'", "\"")
    private var operators = listOf("+", "-", "*", "/", "%", "=", "==", "!=", "<", "<=", ">", ">=", "&&", "||", "!")
    private var reservedWords =
        listOf("int", "string", "char", "read", "readInt", "if", "else", "display", "while", "def", "return", "const")
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
            while (program.index < program.codeLength && program.code[program.index].isWhitespace()) {
                if (program.code[program.index] == '\n') {
                    program.lineIndex++
                }
                program.index++
            }
        }

        fun overcomeComments(){
            while(program.index < program.codeLength && program.code[program.index] =='/'){
                if(program.index + 1 < program.codeLength && program.code[program.index + 1] == '/'){
                    while(program.index < program.codeLength && program.code[program.index] != '\n'){
                        program.index++
                    }
                }
            }
        }

        overcomeSpaces()
        overcomeComments()
    }

    private fun scan() {
        val file: Path = Path("src/main/kotlin/input/" + fileName)
        val program: Program = Program(Files.readString(file))


    }

}