fun main(args: Array<String>) {
    //checkExample("level5", Level5::solve)

    //solveSingleAndWrite("level5", "1", Level5::solve)
    solveSingleAndWrite("level5", "2", Level5::solve)
    //solveSingleAndWrite("level5", "3", Level5::solve)
    //solveSingleAndWrite("level5", "4", Level5::solve)
    //solveSingleAndWrite("level5", "5", Level5::solve)

    //solveAll("level5", Level5::solve)
}

object Level5 {

    fun solve(level: String, example: String): String {
        val lines = readInput(level, example)

        val mapSize = lines.first().toInt()
        val map = lines.drop(1).take(mapSize).map { it.trim().split("").filter { x -> x.isNotBlank() } }
        val islands = buildIsland(map)

        val waterRoutesAroundIsland = lines.drop(1 + mapSize + 1)
            .map { Coordinate.fromString(it) }
            .map { point ->
                val island = islands.islands.first { it.points.contains(point) }
                val availablePoints = findWaterBorder(map, island)
                buildPath(availablePoints)
            }

        return waterRoutesAroundIsland.map { coordinates -> coordinates.map { it.toString() }.joinToString(" ") }
            .joinToString("\n")
    }

    fun buildPath(points: Set<Coordinate>): List<Coordinate> {
        val availablePoints = points.toMutableSet()
        val path = mutableListOf<Coordinate>()
        val start = availablePoints.first()
        path.add(start)
        availablePoints.remove(start)
        var current = start
        while (availablePoints.isNotEmpty()) {
            var neighbour = availablePoints.firstOrNull { current.neighbours(it) }
            if (neighbour == null) {
                neighbour = availablePoints.firstOrNull { current.neighboursDiagonally(it) }
                if (neighbour == null) {
                    error("oh no")
                }
            }
            path.add(neighbour)
            availablePoints.remove(neighbour)
            current = neighbour
        }
        return path.toList()
    }


    fun findWaterBorder(map: List<List<String>>, island: Island): Set<Coordinate> {
        val pointsNextToWater = island.points.flatMap {
            it.getAllNeighbours(map.size).filter { n -> map[n.x][n.y] == "W"}
        }
        return pointsNextToWater.toSet()
    }

    fun buildIsland(map: List<List<String>>): IslandMap {
        val finalIslands = mutableSetOf<Island>()
        //val islandFromTop = buildIslandFromTop(map).islands.toMutableSet()
        val islandFromBottom = buildIslandFromTop(map).islands.toMutableSet()

        while (islandFromBottom.isNotEmpty()) {
            val current = islandFromBottom.first()
            var foundAtAll = false
            for (final in finalIslands) {
                if (final.neighbours(current)) {
                    final.points.addAll(current.points)
                    islandFromBottom.remove(current)
                    foundAtAll = true
                    break
                }
            }
            if (!foundAtAll) {
                finalIslands.add(current)
                islandFromBottom.remove(current)
            }
        }
        return IslandMap(finalIslands)
    }

    fun buildIslandFromTop(map: List<List<String>>): IslandMap {
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

    fun buildIslandFromBottom(map: List<List<String>>): IslandMap {
        val coordinateToIsland = mutableMapOf<Coordinate, Island>()

        for (xCoord in map.indices) {
            for (yCoord in map.indices) {
                val point = map[xCoord][yCoord]
                if (point == "L") {
                    val newPoint = Coordinate(xCoord, yCoord)
                    if (xCoord < map.size - 1) {
                        val rightPoint = map[xCoord + 1][yCoord]
                        if (rightPoint == "L") {
                            val right = Coordinate(xCoord + 1, yCoord)
                            val island = coordinateToIsland[right]
                                ?: error("Should have found island for point right of $xCoord / $yCoord")
                            island.points.add(newPoint)
                            coordinateToIsland[newPoint] = island
                            continue
                        }
                    }
                    if (yCoord < map.size - 1) {
                        val lowerPoint = map[xCoord][yCoord + 1]
                        if (lowerPoint == "L") {
                            val down = Coordinate(xCoord, yCoord + 1)
                            val island = coordinateToIsland[down]
                                ?: error("Should have found island for point below of $xCoord / $yCoord")
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

    data class Island(val points: MutableSet<Coordinate>) {

        fun neighbours(other: Island): Boolean {
            return points.any { my -> other.points.any { other -> other.neighbours(my) } }
        }
    }
}
