import kotlin.random.Random
import kotlin.system.exitProcess

class SudokuGame(private val difficulty: Int) {
    private val grid = Array(9) { IntArray(9) }
    private val solution = Array(9) { IntArray(9) }
    private var startTime: Long = 0
    private var bestTime: Long = Long.MAX_VALUE

    fun start() {
        generateGrid()
        printGrid()
        startTime = System.currentTimeMillis()

        while (!isGameComplete()) {
            print("\nВведите строку, столбец и значение (например, 1 2 3):")
            val input = readLine() ?: ""
            val values = input.split(" ").map { it.toIntOrNull() }
            if (values.size == 3) {
                val (row, col, value) = values

                if (row != null && col != null && value != null) {
                    if (
                        row in 1..9
                        && col in 1..9
                        && value in 1..9
                    ) {
                        if (grid[row - 1][col - 1] == 0) {

                            if (isMoveValids(row - 1, col - 1, value)) {
                                grid[row - 1][col - 1] = value
                                printGrid()
                            } else {
                                println("Неверный ход! Попробуйте еще раз.")
                            }

                        } else {
                            println("Ячейка уже занята! Попробуйте еще раз.")
                        }
                    } else {
                        println("Неверный Ввод! Попробуйте еще раз.")
                    }
                }


            } else if (input.toLowerCase() == "решать") {
                solveGrid()
                printGrid()
                break
            } else {
                println("Неверный Ввод! Попробуйте еще раз.")
            }
        }

        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime
        if (elapsedTime < bestTime) {
            bestTime = elapsedTime
            println("\nНовое лучшее время: ${formatTime(elapsedTime)}")
        } else {
            println("\nВаше время: ${formatTime(elapsedTime)}")
            println("\nЛучшее время: ${formatTime(bestTime)}")
        }
    }

    private fun generateGrid() {
        // Generate a random complete solution
        solveGrid()

        // Remove cells based on the desired difficulty level
        val cellsToRemove = when (difficulty) {
            1 -> 30..40
            2 -> 41..50
            else -> 51..60
        }
        for (row in 0 until 9) {
            for (col in 0 until 9) {
                if (Random.nextInt(100) !in cellsToRemove) {
                    grid[row][col] = solution[row][col]
                }
            }
        }
    }

    private fun solveGrid() {
        for (row in 0 until 9) {
            for (col in 0 until 9) {
                solution[row][col] = grid[row][col]
            }
        }

        solve(0, 0)
    }

    private fun solve(row: Int, col: Int): Boolean {
        if (row == 9) return true
        if (col == 9) return solve(row + 1, 0)
        if (solution[row][col] != 0) return solve(row, col + 1)

        for (value in 1..9) {
            if (isMoveValid(row, col, value)) {
                solution[row][col] = value
                if (solve(row, col + 1)) return true
                solution[row][col] = 0
            }
        }

        return false
    }

    private fun isMoveValid(row: Int, col: Int, value: Int): Boolean {
        for (i in 0 until 9) {
            if (solution[row][i] == value || solution[i][col] == value) {
                return false
            }
        }

        val gridRow = row / 3 * 3
        val gridCol = col / 3 * 3
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (solution[gridRow + i][gridCol + j] == value) {
                    return false
                }
            }
        }

        return true
    }
    private fun isMoveValids(row: Int, col: Int, value: Int): Boolean {
//        val gridRow = row / 3 * 3
//        val gridCol = col / 3 * 3
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                if (solution[row][col] == value) {
                    println(solution[row][col])
                    return true
                }
            }
        }

        return false
    }

    private fun isGameComplete(): Boolean {
        for (row in 0 until 9) {
            for (col in 0 until 9) {
                if (grid[row][col] == 0) {
                    return false
                }
            }
        }
        return true
    }

    private fun printGrid() {
        println("\n   1 2 3   4 5 6   7 8 9")
        for (row in 0 until 9) {
            if (row % 3 == 0) {
                println("  +-------+-------+-------+")
            }
            print("${row + 1} ")
            for (col in 0 until 9) {
                if (col % 3 == 0) {
                    print("| ")
                }
                print(if (grid[row][col] != 0) "${grid[row][col]} " else "  ")
            }
            println("|")
        }
        println("  +-------+-------+-------+")
    }

    private fun formatTime(timeMillis: Long): String {
        val minutes = (timeMillis / 1000) / 60
        val seconds = (timeMillis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}

fun main() {
    println("Добро пожаловать в судоку!")
    println("Выберите уровень сложности (1-3):")
    val difficulty = readLine()?.toIntOrNull() ?: 1
    if (difficulty !in 1..3) {
        println("\nНеверный уровень сложности! Выход...")
        exitProcess(0)
    }

    val game = SudokuGame(difficulty)
    game.start()
}
