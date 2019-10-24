/*
  Author: Ryan Flageolle
  Assignment: Create an array of 1000 ints. Search for a value known to be in
  the array by generating an index at random and testing to see if the searched
  for int is in the array at that index. Write your program to make at most
  5000 guesses. Try 100 different searches and compute the average number of
  comparisons the program has to do.
*/
import scala.util.Random
import scala.util.control.Breaks._

object ArraySearch{
  val array = Seq.fill( 1000 )( Random.nextInt(9999) ).toArray

  def pickRandomIndex(): Int = {
    array( Random.nextInt(1000) )
  }

  def oneTest( i: Int ): Boolean = {
    array( Random.nextInt(1000) ) == i
  }

  def simulate(debug: Boolean): Int = {
    val test = pickRandomIndex
    var found = false
    var count = 0
    breakable {
      for (_ <- 0 to 5000 ) {
        if ( oneTest(test) ) {
          found = true
          break
        } else {
           count += 1
         }
      }
    }

    if (debug) {
      if (found) {
        println(s"Random Search successfully found: $test in $count attempts.")
      } else {
        println(s"Random Search was unable to find: $test")
      }
    }

    count
  }

  def manyTrials(n: Int): Unit = {
    var total = 0
    for (_ <- 0 to n) {
      total += simulate(false)
    }
    val average = ( total / n )
    println(s"Through $n trials - Random Search takes on average $average attempts to guess the correct value.")
  }
}

ArraySearch.manyTrials(1000)
