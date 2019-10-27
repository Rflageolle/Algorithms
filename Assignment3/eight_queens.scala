/*
  Author: Ryan Flageolle
  Assignment: Compare Dart throwing and mean of values at random locations
  to the trapezoid method. Compare both acccuracy and running times. Make sure
  to use interesting functions for your tests.
*/
import scala.util.Random


class Queen(val col: Int, val row: Int)

object Board {
  var matrix = Array.ofDim[Int](8, 8)
  var queens = new List()

  def eightQueens(): Unit {

  }

  def placeQueen(): Unit {
    val n = new Queen(Random.nextInt(8), Random.nextInt(8))

  }

  def sameColumn(q: Queen): Boolean {
    for (n <- 0 to 7) {

    }
  }

  def sameRow(q: Queen): Boolean {

  }

  def onDiag(q: Queen): Boolean {

  }
}
