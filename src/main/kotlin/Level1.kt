fun main(args: Array<String>) {
    //checkExample("level1", Level1::solve)

    //solveSingleAndWrite("level1", "1", Level1::solve)
    //solveSingleAndWrite("level1", "2", Level1::solve)
    //solveSingleAndWrite("level1", "3", Level1::solve)
    //solveSingleAndWrite("level1", "4", Level1::solve)
    //solveSingleAndWrite("level1", "5", Level1::solve)

    solveAll("level1", Level1::solve)
}

object Level1 {

    fun solve(level: String, example: String): String {
        val lines = readInput(level, example)

        val mapSize = lines.first().toInt()
        val map = lines.drop(1).take(mapSize).map { it.trim().split("").filter { x -> x.isNotBlank() } }

        val coordinates = lines.drop(1 + mapSize + 1).map { it.split(",").map { x -> x.toInt() } }
        println(coordinates)

        return coordinates.map {
            val y = it[0]
            val x = it[1]
            map[x][y]
        }.joinToString("\n")
    }

}
