/*
* Author: Ryan Flageolle
* Assignment: Simulate the throwing of the darts and printout the approximate
    value of Π computed for various iterations such as 1000, 10,000, 100,000,
    1,000,000 and 100,000,000 (if time allows (it should)).
*/

import scala.util.Random
import scala.math.{sqrt, exp}

// object which represents a "dart's" location on the board
case class Dart(x: Double, y: Double)

/*
  n is used to determine how many darts will be thrown in a session
*/
class DartSession(val n: Int) {

  // return a randomly generated dart position with x and y between -1 and 1
  def throwDart(): Dart = {
    val random = new Random();
    val xbool = random.nextBoolean()
    val x = if (xbool) (random.nextDouble() * (-1)) else (random.nextDouble() * (1))
    val ybool = random.nextBoolean()
    val y = if (ybool) (random.nextDouble() * (-1)) else (random.nextDouble() * (1))

    new Dart(x, y)
  }

  // if distance from origin greater than 1 than not in circle
  def isInCircle(d: Dart): Boolean = {
    val dist = sqrt( (((d.x) - 0) * ((d.x) - 0)) + (((d.y) - 0) * ((d.y) - 0)) )
    val is = if (dist < 1.0) true else false
    is
  }

  // returns number of darts inside circle (M)
  def session(): Int = {
    var count = 0
    for (a <- 0 to this.n) {
      // println(s"----Throw #$a----")
      var check = if (this.isInCircle(this.throwDart())) 1 else 0
      // println(s"check = $check \n --------")
      count = count + check
    }
    count
  }

  def results(m: Int): Double = (4.0 * m) / n

  // driver function for the simulation
  def simulate(): Unit = {
    val m = this.session()
    val r = this.results(m)
    println(s"The approximation of 𝜋, throwing ${this.n} darts is: $r")
  }
}

val sessions = List(1000, 10000, 100000, 1000000, 100000000)
sessions.foreach(n => {
  var s = new DartSession(n)
  s.simulate()
})
