
/*
  Author: Ryan Flageolle
  Assignment: Compare Dart throwing and mean of values at random locations
  to the trapezoid method. Compare both acccuracy and running times. Make sure
  to use interesting functions for your tests.
*/
import scala.util.Random


class Board(val n: Int) {
  var matrix = Array.ofDim[Boolean](this.n, this.n)

  def clearBoard(): Unit = {
    this.matrix = Array.ofDim[Boolean](this.n, this.n)
  }

  def printBoard(): Unit = {
    for ( row <- 0 to (this.n - 1) ) {
      var str = ""
      for ( col <- 0 to (this.n - 1) ) {
        if (this.matrix(row)(col)) str += "| Q " else str += "| _ "
      }
      println(str + "|")
    }
    println("")
    clearBoard
  }

  def placeQueen(row: Int, col: Int): Unit = {
    this.matrix(row)(col) = true
  }

  def removeQueen(row: Int, col: Int): Unit = {
    this.matrix(row)(col) = false
  }

  def onBoard(row: Int, col: Int): Boolean = {
    return (row >= 0 && row < this.n && col >= 0 && col < this.n)
  }

  def goRight(row: Int, col: Int, legal: Boolean): Boolean = {
    if (onBoard(row, col + 1) && legal) {
      return (goRight(row, col + 1, !this.matrix(row)(col + 1)))
    } else {
      return legal
    }
  }

  def goLeft(row: Int, col: Int, legal: Boolean): Boolean = {
    if (onBoard(row, col - 1) && legal) {
      return (goLeft(row, col - 1, !this.matrix(row)(col - 1)))
    } else {
      return legal
    }
  }

  def safeRow(row: Int, col: Int): Boolean = {
    return (goRight(row, col, true) && goLeft(row, col, true))
  }

  def upRight(row: Int, col: Int, legal: Boolean): Boolean = {
    if (onBoard((row - 1), (col + 1)) && legal) {
      return upRight((row - 1), (col + 1), !this.matrix(row - 1)(col + 1))
    } else {
      return legal
    }
  }

  def upLeft(row: Int, col: Int, legal: Boolean): Boolean = {
    if (onBoard((row - 1), (col - 1)) && legal) {
      return upLeft((row - 1), (col - 1), !this.matrix(row - 1)(col - 1))
    } else {
      return legal
    }
  }

  def downRight(row: Int, col: Int, legal: Boolean): Boolean = {
    if (onBoard((row + 1), (col + 1)) && legal) {
      return downRight((row + 1), (col + 1), !this.matrix(row + 1)(col + 1))
    } else {
      return legal
    }
  }

  def downLeft(row: Int, col: Int, legal: Boolean): Boolean = {
    if (onBoard((row + 1), (col - 1)) && legal) {
      return downLeft((row + 1), (col - 1), !this.matrix(row + 1)(col - 1))
    } else {
      return legal
    }
  }

  def safeDiagonal(row: Int, col: Int): Boolean = {
    return (upRight(row, col, true) && upLeft(row, col, true) && downRight(row, col, true) && downLeft(row, col, true))
  }

  def legalMove(row: Int, col: Int): Boolean = {
    return safeDiagonal(row, col) && safeRow(row, col)
  }

  def randRow(): Int = {
    return Random.nextInt(this.n)
  }

  def randomLegalQueen(col: Int): Unit = {
    var row = randRow

    while (!legalMove(row, col)) {
      row = randRow
    }

    placeQueen(row, col)
  }

  def placeRandK(k: Int): Unit = {
    for (col <- 0 to (k - 1)) {
      randomLegalQueen(col)
    }
  }

}

class Tracker(val n: Int) {
  var b = new Board(n)

  def backtracking(col: Int): Boolean = {
    if (col == (this.n)) {
      return true
    }

    for (row <- 0 to (this.n - 1)) {
      if (this.b.legalMove(row, col)) {
        this.b.placeQueen(row, col)

        if (backtracking(col + 1)) {
          return true
        }

        this.b.removeQueen(row, col)
      }
    }

    return false
  }

  def backtrackOnly(): Unit = {
    if (backtracking(0)) {
      this.b.printBoard
    } else {
      println("No Solution found")
    }

  }

  def backtrackRand(k: Int): Unit = {
    this.b.placeRandK(k)
    if (backtracking(0)) {
      this.b.printBoard
    } else {
      println("No Solution found")
    }
  }

  def timer(k: Int): Int = {
    val start = System.currentTimeMillis
    if (k == 0) backtrackOnly() else backtrackRand(k)
    return (System.currentTimeMillis - start).toInt
  }

  def comparison(): (Int, Int) = {
    var times = Array.ofDim[Int](this.n)
    var index = 0
    var fastest = times(index)

    for (k <- 0 to (this.n - (this.n * 0.25).toInt)) {
      println(s"k: $k")
      val time = timer(k)
      times(k) = time
      println(time)
    }

    for (n <- 0 to (this.n - 1)) {
      index = if (fastest < times(n)) n else index
    }

    println(s"when k = ${index}  duration = ${fastest}")
    return (index, fastest)
  }

  def analysis(trials: Int): Unit = {
    var list = Array.ofDim[Long](this.n, 2)
    var bestK = 0

    for (index <- 0 to trials) {
      val trial = comparison()
      println(s"trial #$index:  index = ${trial._1}   time = ${trial._2}")
      list(trial._1)(0) += 1
      list(trial._1)(1) += trial._2
    }

    for (item <- 0 to (this.n - 1)) {
      println(s"index #$item: count = ${list(item)(0)}  time = ${list(item)(1)}")
      bestK = if (list(item)(0) > list(bestK)(0)) item else bestK
    }

    val avg = list(bestK)(1) / list(bestK)(0)

    println(s"For ${trials} trials: The best value of K is ${bestK} with an average runTime of ${avg} seconds")
  }
}


val trial = new Tracker(8)
trial.analysis(10)
