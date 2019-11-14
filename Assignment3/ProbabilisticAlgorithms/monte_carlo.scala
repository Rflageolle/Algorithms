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
  println(s"Darts (after $trials trials):  The estimated integral of f(x) = $guess")

}

def mean(f: (Double) => Double, start: Int, stop: Int, trials: Int): Unit = {
  var sum: Double = 0
  for (_ <- 0 to trials) {
    val x = (Random.nextDouble() * (stop - start)) + start
    sum += f(x)
  }

  val guess = (sum / trials) * (stop - start)
  println(s"Mean (after $trials trials):  The estimated integral of f(x) = $guess")
}

def trapezoid(f: (Double) => Double, start: Int, stop: Int, trials: Int): Unit = {
  val h = (stop - start).toDouble / trials
  var s = (f(start) + f(stop))

  var trial = 0
  while (trial < trials) {
    s += 2 * f(start + trial * h)
    trial += 1
  }

  val guess = ((h / 2) * s)
  println(s"Trapezoid (after $trials trials):  The estimated integral of f(x) = $guess")
}

val f = (x: Double) => abs(((cos(3 * Pi * x)) + (4 * (sin(x))))+5)

darts(f, 0, 10, 10, 100000)
mean(f, 0, 10, 100000)
trapezoid(f, 0, 10, 100000)
