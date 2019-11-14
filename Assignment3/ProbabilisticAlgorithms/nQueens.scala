import scala.util.Random

class Board(n: Int) {
  var matrix = Array.ofDim[Boolean](this.n, this.n)

  def printBoard(): Unit = {
    for ( row <- 0 to (this.n - 1) ) {
      var str = ""
      for ( col <- 0 to (this.n - 1) ) {
        if (this.matrix(row)(col)) str += "| Q " else str += "| _ "
      }
      println(str + "|")
    }
    println("")
  }

  def isOccupied(row: Int, col: Int): Boolean = {
    return this.matrix(row)(col)
  }

  def placeQueen(row: Int, col: Int): Unit = {
    this.matrix(row)(col) = true
  }

  def removeQueen(row: Int, col: Int): Unit = {
    this.matrix(row)(col) = false
  }

  def onBoard(row: Int, col: Int): Boolean = {
    return (row >= 0 && row < this.n) && (col >= 0 && col < this.n)
  }

  def upAndLeft(row: Int, col: Int, legal: Boolean): Boolean = {
    if (onBoard(row - 1, col - 1) && legal) {
      return upAndLeft(row - 1, col - 1, !isOccupied(row - 1, col - 1))
    } else {
      return legal
    }
  }

  def downAndLeft(row: Int, col: Int, legal: Boolean): Boolean = {
    if (onBoard(row + 1, col - 1) && legal) {
      return downAndLeft(row + 1, col - 1, !isOccupied(row + 1, col - 1))
    } else {
      return legal
    }
  }

  def left(row: Int, col: Int, legal: Boolean): Boolean = {
    if (onBoard(row, col - 1) && legal) {
      return left(row, col - 1, !isOccupied(row, col - 1))
    } else {
      return legal
    }
  }

  def isLegalMove(row: Int, col: Int): Boolean = {
    return upAndLeft(row, col, true) && downAndLeft(row, col, true) && left(row, col, true)
  }
}

class Solution(n: Int) {
  var b = new Board(this.n)

  def newBoard(): Unit = {
    this.b = new Board(this.n)
  }

  def safeQueen(col: Int): Boolean = {
    var row = Random.nextInt(this.n)
    var attempts = 0

    while (!this.b.isLegalMove(row, col) && attempts < 100000) {
      row = Random.nextInt(this.n)
      attempts += 1
    }

    if (attempts == 100000) {
      return false
    } else {
      this.b.placeQueen(row, col)
      return true
    }
  }

  def nQueens(k: Int): Unit = {
    var col = 0
    var placed = true

    while (col < k && placed) {
      placed = safeQueen(col)
      col += 1
    }

    if (!placed) {
      fixRandomPlacements(col - 1)
    }

  }

  def backtracking(col: Int): Boolean = {
    if (col == (this.n)) {
      return true
    }

    for (row <- 0 to (this.n - 1)) {
      if (this.b.isLegalMove(row, col)) {
        this.b.placeQueen(row, col)

        if (backtracking(col + 1)) {
          return true
        }

        this.b.removeQueen(row, col)
      }
    }

    return false
  }

  def fixRandomPlacements(col: Int): Unit = {
    if (!backtracking(col)) {
      for (row <- 0 to (this.n - 1)) {
        if (this.b.isOccupied(row, col)) {
          this.b.removeQueen(row, col)
        }
      }
      this.fixRandomPlacements(col - 1)
    }

  }

  def timer(k: Int): Long = {
    newBoard()
    val start = System.currentTimeMillis
    nQueens(k)
    val complete = backtracking(k)
    val time = System.currentTimeMillis - start

    // if (complete) {
    //   println(s"k = $k completed in $time milliseconds")
    // } else {
    //   println(s"k = $k completed with no solution found in $time milliseconds")
    // }

    return time
  }

  def runSimulation(trials: Int): Unit = {
    var times = Array.ofDim(this.n)
    var fastest = 10000.toLong
    var bestK = 0

    for (_ <- 1 to trials) {
      for (k <- 0 to (this.n - (0.25 * this.n).toInt)) {
        newBoard()
        val current = timer(k)

        if (current < fastest) {
          fastest = current
          bestK = k
        }
      }
    }

    println(s"After $trials trials: K = $bestK  completes the fastest in $fastest milliseconds")
  }



  def simulation(k: Int, trials: Int): Unit = {
    var times = new Array[Long](trials)

    for (index <- 0 to (trials - 1)) {
      times(index) = timer(k)
    }
    var total = 0.toLong
    times.foreach(v => total += v)
    val avg = total / trials

    println(s"After $trials trials: k = $k takes on average $avg milliseconds")
  }

}

val s = new Solution(8)
// s.simulation(0, 10000)
// s.simulation(1, 10000)
// s.simulation(2, 10000)
s.runSimulation(10)
