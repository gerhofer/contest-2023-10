import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path

const val CCC_FOLDER = "C:\\Users\\Pia\\IdeaProjects\\CodingContest202310\\src\\main\\resources"

fun checkExample(level: String, solve: (level: String, example: String) -> String) {
    val result = solve(level, "example")
    val expectedResult = Files.readAllLines(Path(getOutPath(level, "example")))
        .joinToString("\n")
    if (result == expectedResult) {
        println("YES")
        println("Result $result matches expected $expectedResult")
    } else {
        println("NOPE")
        println("Result $result does not match expected $expectedResult")
    }
}

fun solveSingleAndWrite(level: String, example: String, solve: (level: String, example: String) -> String) {
    val result = solve(level, example)
    writeResult(level, example, result)
}

fun solveAll(level: String, solve: (level: String, example: String) -> String) {
    val examples = listOf("example", "1", "2", "3", "4", "5")
    for (example in examples) {
        solve(level, example)
    }
}

fun getInPath(level: String, example: String): String {
    return "${CCC_FOLDER}\\${level}\\${level}_${example}.in"
}

fun getOutPath(level: String, example: String): String {
    return "${CCC_FOLDER}\\${level}\\${level}_${example}.out"
}

fun readInput(level: String, example: String) : List<String> {
    return Files.readAllLines(Path(getInPath(level, example)))
}

fun writeResult(level: String, example: String, result: String) {
    File(getOutPath(level, example)).writeText(result)
    println("$example:")
    println(result)
}