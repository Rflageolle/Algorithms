/*
  Author: Ryan Flageolle
  Assignment: Solve the 8 queens problem by gluing k random queens to the board
  and placing the other 8-k queens using backtracking. What value for k gives
  the best result (on avereage)? How does the running time compare to the
  traditional backtracking algorithm?
*/
import scala.util.Random

/*

*/
class Board(n: Int) {
  var matrix = Array.ofDim[Boolean](this.n, this.n)

  // debug and test function
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

  // Methods to determine if placing a queen is legal
  def onBoard(row: Int, col: Int): Boolean = {
    return (row >= 0 && row < this.n) && (col >= 0 && col < this.n)
  }

  // I decided to attempt to make this a less costly operation by making it
  // recursive. probes diagonly up and left
  def upAndLeft(row: Int, col: Int, legal: Boolean): Boolean = {
    if (onBoard(row - 1, col - 1) && legal) {
      return upAndLeft(row - 1, col - 1, !isOccupied(row - 1, col - 1))
    } else {
      return legal
    }
  }

  // probes diagonly down and left
  def downAndLeft(row: Int, col: Int, legal: Boolean): Boolean = {
    if (onBoard(row + 1, col - 1) && legal) {
      return downAndLeft(row + 1, col - 1, !isOccupied(row + 1, col - 1))
    } else {
      return legal
    }
  }

  //probes to the left
  def left(row: Int, col: Int, legal: Boolean): Boolean = {
    if (onBoard(row, col - 1) && legal) {
      return left(row, col - 1, !isOccupied(row, col - 1))
    } else {
      return legal
    }
  }

  // combines the above methods into one call
  def isLegalMove(row: Int, col: Int): Boolean = {
    return upAndLeft(row, col, true) && downAndLeft(row, col, true) && left(row, col, true)
  }

  // end legal moves methods
}

/*

*/
class Solution(n: Int) {
  var b = new Board(this.n)

  // to clear board between test calls
  def newBoard(): Unit = {
    this.b = new Board(this.n)
  }

  // this method places a random queen in a provided column, it makes sure that
  // the spot is safe before placing it, this caused issues in the last few
  // columns which I fixed by using a counter to limit the number of attempts.
  // if the number of attempts is less than 100000 it places the queen and
  // returns true else it only returns false.
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

  // runs safeQueen k times, if safeQueen returns false meaning a randomly
  // generated queen is taking to long to place fixRandomPlacements takes over.
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

  // from the provided column, the standard backtracking algorithm kicks in
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

  // this uses backtracking to solve a board with completely randomly placed
  // queens which if needed backtracks one row and calls backtracking again
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

  // adds a timer function
  def timer(k: Int): Long = {
    newBoard()
    val start = System.currentTimeMillis
    nQueens(k)
    val complete = backtracking(k)
    val time = System.currentTimeMillis - start

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

  // driver function
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
s.runSimulation(10)
