fun main(args: Array<String>) {
    //checkExample("level4", Level4::solve)

    //solveSingleAndWrite("level4", "1", Level4::solve)
    //solveSingleAndWrite("level4", "2", Level4::solve)
    //solveSingleAndWrite("level4", "3", Level4::solve)
    //solveSingleAndWrite("level4", "4", Level4::solve)
    solveSingleAndWrite("level4", "5", Level4::solve)

    //solveAll("level4", Level4::solve)
}

object Level4 {

    fun solve(level: String, example: String): String {
        val lines = readInput(level, example)

        val mapSize = lines.first().toInt()
        val map = lines.drop(1).take(mapSize).map { it.trim().split("").filter { x -> x.isNotBlank() } }

        val plannedRoutes = lines.drop(1 + mapSize + 1)
            .map { it.split(" ") }
            .map { points -> points.map { Coordinate.fromString(it) } }
            .map { coords -> planRoute2(coords[0], coords[1], map) }

        return plannedRoutes
            .map { coords -> coords.joinToString(" ") }
            .joinToString("\n")
    }

    fun planRoute(start: Coordinate, end: Coordinate, map: List<List<String>>): List<Coordinate> {
        if (map[end.x][end.y] != "W") {
            error("End is not in water")
        }
        val currentRoute = mutableListOf<Coordinate>()
        val visited = mutableSetOf<Coordinate>()
        if (move(start, end, map, currentRoute, visited)) {
            return currentRoute
        }
        error("no route found")
    }

    fun planRoute2(start: Coordinate, end: Coordinate, map: List<List<String>>): List<Coordinate> {
        if (map[end.x][end.y] != "W") {
            error("End is not in water")
        }
        val currentRoute = mutableListOf<Coordinate>()
        val shortesPathsPerPoint: List<MutableList<List<Coordinate>?>> = map.map { line -> line.map { point -> null }.toMutableList() }
        makeAllMovements(start, end, map, currentRoute, shortesPathsPerPoint)
        return (shortesPathsPerPoint[end.x][end.y] ?: error("no route found")) + end
    }

    fun makeAllMovements(current: Coordinate,
             end: Coordinate,
             map: List<List<String>>,
             currentPath: List<Coordinate>,
             shortestPathsToPoint: List<MutableList<List<Coordinate>?>>,
    ) {
        if (current == end) {
            return
        }
        if (currentPath.size > map.size*2) {
            return
        }
        if (currentPath.contains(current)) {
            return
        } else if (map[current.x][current.y] != "W"){
            return
        } else if (routeCrossesDiagonally(currentPath + current)) {
            return
        }
        if ((current.cartesianDistance(end) + currentPath.size) > map.size*2) {
            return
        }

        val possibleNeighbours = current.getAllNeighbours(map.size).sortedByDescending { it.sortValue(end) }
        val neighboursToContinueFrom = mutableListOf<Coordinate>()
        for (neighbour in possibleNeighbours) {
            val currentShortest = shortestPathsToPoint[neighbour.x][neighbour.y]
            if (currentShortest == null || currentShortest.size > (currentPath.size + 1)) {
                shortestPathsToPoint[neighbour.x][neighbour.y] = currentPath + current
                neighboursToContinueFrom.add(neighbour)
            }
        }
        if (shortestPathsToPoint[end.x][end.y] != null && shortestPathsToPoint[end.x][end.y]!!.size < (map.size*2 - 1)) {
            return
        }
        for (neighbour in neighboursToContinueFrom) {
            makeAllMovements(neighbour, end, map, currentPath + current, shortestPathsToPoint)
        }
    }

    fun move(current: Coordinate, end: Coordinate, map: List<List<String>>, currentRoute: MutableList<Coordinate>, forbiddenFields: MutableSet<Coordinate>) : Boolean{
        if ((current.cartesianDistance(end) + currentRoute.size) > map.size*2) {
            return false
        }
        if (currentRoute.size >= map.size*2) {
            return false
        }
        if (current == end) {
            currentRoute.add(current)
            return true
        } else if (currentRoute.contains(current)) {
            return false
        } else if (map[current.x][current.y] != "W"){
            forbiddenFields.add(current)
            return false
        } else if (routeCrossesDiagonally(currentRoute + current)) {
            return false
        }

        currentRoute.add(current)
        println(currentRoute.map { it.toString() }.joinToString(" "))
        val possibleNeighbours = current.getAllNeighbours(map.size).sortedByDescending { it.sortValue(end) }
        for (neighbour in possibleNeighbours) {
            val found = move(neighbour, end, map, currentRoute, forbiddenFields)
            if (found) {
                println("found path")
                return true
            }
        }
        val removedFromPath = currentRoute.removeLast()
        forbiddenFields.add(removedFromPath)
        return false
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
