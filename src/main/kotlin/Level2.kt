import kotlin.math.abs

fun main(args: Array<String>) {
    //checkExample("level2", Level2::solve)

    //solveSingleAndWrite("level2", "1", Level2::solve)
    //solveSingleAndWrite("level2", "2", Level2::solve)
    //solveSingleAndWrite("level2", "3", Level2::solve)
    //solveSingleAndWrite("level2", "4", Level2::solve)
    //solveSingleAndWrite("level2", "5", Level2::solve)

    solveAll("level2", Level2::solve)
}

object Level2 {

    const val SAME = "SAME"
    const val DIFFERENT = "DIFFERENT"

    fun solve(level: String, example: String): String {
        val lines = readInput(level, example)

        val mapSize = lines.first().toInt()
        val map = lines.drop(1).take(mapSize).map { it.trim().split("").filter { x -> x.isNotBlank() } }

        return lines.drop(1 + mapSize + 1)
            .map { it.split(" ") }
            .map { points ->
                val coordinates = points.map { Coordinate.fromString(it) }
                val visited = mutableSetOf<Coordinate>()
                touches(coordinates[0], coordinates[1], map, visited)
            }
            .map {
                if (it) {
                    SAME
                } else {
                    DIFFERENT
                }
            }.joinToString("\n")
    }

    fun touches(
        current: Coordinate,
        goal: Coordinate,
        field: List<List<String>>,
        visited: MutableSet<Coordinate>
    ): Boolean {
        if (visited.contains(current)) {
            return false
        }
        if (current == goal) {
            return true
        }
        visited.add(current)
        if (field[current.x][current.y] == "L") {
            val neighbours = current.getNeighbours(field.size)
            return neighbours.any { touches(it, goal, field, visited) }
        }
        return false
    }

    fun buildIsland(map: List<List<String>>): IslandMap {
        val coordinateToIsland = mutableMapOf<Coordinate, Island>()

        for (xCoord in map.indices) {
            for (yCoord in map.indices) {
                val point = map[xCoord][yCoord]
                if (point == "L") {
                    val newPoint = Coordinate(xCoord, yCoord)
                    if (xCoord > 0) {
                        val leftPoint = map[xCoord - 1][yCoord]
                        if (leftPoint == "L") {
                            val left = Coordinate(xCoord - 1, yCoord)
                            val island = coordinateToIsland[left]
                                ?: error("Should have found island for point left of $xCoord / $yCoord")
                            island.points.add(newPoint)
                            coordinateToIsland[newPoint] = island
                            continue
                        }
                    }
                    if (yCoord > 0) {
                        val upperPoint = map[xCoord][yCoord - 1]
                        if (upperPoint == "L") {
                            val up = Coordinate(xCoord, yCoord - 1)
                            val island = coordinateToIsland[up]
                                ?: error("Should have found island for point above of $xCoord / $yCoord")
                            island.points.add(newPoint)
                            coordinateToIsland[newPoint] = island
                            continue
                        }
                    }
                    val newIsland = Island(mutableSetOf(newPoint))
                    coordinateToIsland[newPoint] = newIsland
                }

            }
        }

        return IslandMap(coordinateToIsland.values.toSet())
    }

    data class IslandMap(val islands: Set<Island>)

    data class Island(val points: MutableSet<Coordinate>)

}
