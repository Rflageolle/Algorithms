/*
  Author: Ryan Flageolle
  Assignment: Compare Dart throwing and mean of values at random locations
  to the trapezoid method. Compare both acccuracy and running times. Make sure
  to use interesting functions for your tests.
*/
import scala.util.Random
import scala.math.{ pow, cos, sin }

case class Dart(x: Int, y: Int)
class Equation(val y: (Int) => Int) // describe as function of x

class Box(val eqn: (Double) => Int, val width: Int, val height: Int) {
  val halfw = width / 2
  val halfh = height / 2
  def xbounds(): Int = ( Random.nextInt(width - (width / 2)) - (width / 2) )
  def ybounds(): Int = ( Random.nextInt(height - (height / 2)) - (height / 2) )
}


class DartThrowing( val b: Box ) {

  // val y = (x: Int) => 3 * ( (x) * (x) ) + 4 * x

  def throwDart(): Dart = {
    new Dart((this.b.xbounds), (this.b.ybounds))
  }

  def below(): Boolean = {
    val d = throwDart
    val test = Dart(d.x, b.eqn(d.x))
    d.y < test.y
  }

  def simulate(n: Int): Unit = {
    var count = 0
    for (_ <- 0 to n) {
      if (below) count += 1
    }
    val percent = count.toDouble / n.toDouble
    val integral = percent * ( (b.width) * (b.height) )
    println(s"The percent of darts below the line was: $percent")
    println(s"The integral from ${-1 * b.halfw} to ${(b.width - b.halfw)} is: $integral")
  }

}

class MeanValues( val eqn: (Int) => Int, val width ) {

  def randomDart(): Dart = {
    def randomVal = ( Random.nextInt(width - (width / 2)) - (width / 2) )
    new Dart( randomVal, eqn(randomVal))
  }

}

val y = (x: Double) => (3 * (cos(x)) + (sin(x))).toInt
val box = new Box(y, 20, 6)
val darts = new DartThrowing(box)

darts.simulate(100000)
