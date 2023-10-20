fun main(args: Array<String>) {
    //checkExample("level3", Level3::solve)

    //solveSingleAndWrite("level3", "1", Level3::solve)
    //solveSingleAndWrite("level3", "2", Level3::solve)
    //solveSingleAndWrite("level3", "3", Level3::solve)
    //solveSingleAndWrite("level3", "4", Level3::solve)
    //solveSingleAndWrite("level3", "5", Level3::solve)

    solveAll("level3", Level3::solve)
}

object Level3 {

    const val VALID = "VALID"
    const val INVALID = "INVALID"

    fun solve(level: String, example: String): String {
        val lines = readInput(level, example)

        val mapSize = lines.first().toInt()
        val map = lines.drop(1).take(mapSize).map { it.trim().split("").filter { x -> x.isNotBlank() } }

        val routes = lines.drop(1 + mapSize + 1)
            .map { it.split(" ") }
            .map { points -> points.map { Coordinate.fromString(it) } }

        var result = ""
        for (route in routes) {
            if (route.size != route.toSet().size) {
                // visit any spot twice
                result += (INVALID + "\n")
            } else if (routeCrossesDiagonally(route)) {
                result += (INVALID + "\n")
            } else {
                result += (VALID + "\n")
            }
        }

        return result.trim()
    }

    fun routeCrossesDiagonally(coordinates: List<Coordinate>): Boolean {
        for (current in coordinates.windowed(2, 1)) {
            if (isDiagonal(current[0], current[1])) {
                if (existsOtherDiagonal(current[0], current[1], coordinates)) {
                    return true
                }
            }
        }
        return false
    }

    fun isDiagonal(pointA: Coordinate, pointB: Coordinate): Boolean {
        return Math.abs(pointA.x - pointB.x) == 1 && Math.abs(pointA.y - pointB.y) == 1
    }

    fun existsOtherDiagonal(pointA: Coordinate, pointB: Coordinate, coordinates: List<Coordinate>): Boolean {
        val invPointA = Coordinate(pointA.x, pointB.y)
        val invPointB = Coordinate(pointB.x, pointA.y)
        if (coordinates.contains(invPointA) && coordinates.contains(invPointB)) {
            val index1 = coordinates.indexOf(invPointA)
            val index2 = coordinates.indexOf(invPointB)
            return Math.abs(index1 - index2) == 1
        }
        return false
    }

}
