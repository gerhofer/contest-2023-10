import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.math.abs
import kotlin.math.pow

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
        solveSingleAndWrite(level, example, solve)
    }
}

fun getInPath(level: String, example: String): String {
    return "${CCC_FOLDER}\\${level}\\${level}_${example}.in"
}

fun getOutPath(level: String, example: String): String {
    return "${CCC_FOLDER}\\${level}\\${level}_${example}.out"
}

fun readInput(level: String, example: String): List<String> {
    return Files.readAllLines(Path(getInPath(level, example)))
}

fun writeResult(level: String, example: String, result: String) {
    File(getOutPath(level, example)).writeText(result)
    println("$example:")
    println(result)
}

data class Coordinate(val x: Int, val y: Int) {

    override fun toString(): String = "$y,$x"

    fun sortValue(other: Coordinate): Int {
        val xDist = abs(x - other.x)
        val yDist = abs(y - other.y)
        if (xDist > yDist) {
            return other.x - x
        } else {
            return other.y - y
        }
    }

    fun cartesianDistance(other: Coordinate): Int {
        return abs(x - other.x) + abs(y - other.y)
    }

    fun polarDistance(other: Coordinate): Double {
        return Math.sqrt((x - other.x).toDouble().pow(2.0) + (y - other.y).toDouble().pow(2.0))
    }

    fun neighbours(other: Coordinate): Boolean {
        return (abs(x - other.x) == 1 && y == other.y) ||
                (abs(y - other.y) == 1 && x == other.x)
    }

    fun neighboursDiagonally(other: Coordinate): Boolean {
        return (abs(x - other.x) == 1 && y == other.y) ||
                (abs(y - other.y) == 1 && x == other.x) ||
                (abs(y - other.y) == 1 && abs(x - other.x) == 1)
    }

    fun getNeighbours(max: Int): List<Coordinate> {
        val neighbours = mutableListOf<Coordinate>()
        if (x > 0) {
            neighbours.add(Coordinate(x - 1, y))
        }
        if (y > 0) {
            neighbours.add(Coordinate(x, y - 1))
        }
        if (x < max - 1) {
            neighbours.add(Coordinate(x + 1, y))
        }
        if (y < max - 1) {
            neighbours.add(Coordinate(x, y + 1))
        }
        return neighbours.toList()
    }

    fun getAllNeighbours(max: Int): List<Coordinate> {
        val neighbours = mutableListOf<Coordinate>()
        if (x > 0) {
            neighbours.add(Coordinate(x - 1, y))
            if (y < max - 1) {
                neighbours.add(Coordinate(x-1, y + 1))
            }
            if (y > 0) {
                neighbours.add(Coordinate(x-1, y - 1))
            }
        }
        if (y > 0) {
            neighbours.add(Coordinate(x, y - 1))
        }
        if (x < max - 1) {
            neighbours.add(Coordinate(x + 1, y))
            if (y < max - 1) {
                neighbours.add(Coordinate(x+1, y + 1))
            }
            if (y > 0) {
                neighbours.add(Coordinate(x+1, y - 1))
            }
        }
        if (y < max - 1) {
            neighbours.add(Coordinate(x, y + 1))
        }
        return neighbours.toList()
    }

    companion object {
        fun fromString(string: String): Coordinate {
            val coords = string.split(",").map { x -> x.toInt() }
            return Coordinate(coords[1], coords[0])
        }
    }
}