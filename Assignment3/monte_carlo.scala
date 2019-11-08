/*
  Author: Ryan Flageolle
  Assignment: Compare Dart throwing and mean of values at random locations
  to the trapezoid method. Compare both acccuracy and running times. Make sure
  to use interesting functions for your tests.
*/
import scala.util.Random
import scala.math.{ pow, cos, sin, Pi, abs }

def darts(f: (Double) => Double, start: Int, stop: Int, maxHeight: Int, trials: Int): Unit = {
  var in = 0
  for (_ <- 0 to trials) {
    val x = (Random.nextDouble() * (stop - start)) + start

    val y = Random.nextDouble() * maxHeight

    if (y <= f(x)) { in = in + 1 }
  }

  val guess = ((stop - start) * maxHeight) * (in / trials.toDouble)
  println(s"The estimated integral of f(x) = ${guess}")

}

val f = (x: Double) => abs(((cos(3 * Pi * x)) + (sin(x)))+5)

darts(f, 0, 10, 10, 100000)
