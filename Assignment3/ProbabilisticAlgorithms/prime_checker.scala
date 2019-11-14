/*
  Author: Ryan Flageolle
  Assignment: Write a program to generate a number randomly and test it against
    k random values.
*/
import scala.util.Random
import scala.util.control.Breaks._

object Composite {
  def isEven = (x: Int) => x % 2 == 0
  def listDigits = (x: Int) => x.toString.map(_.asDigit).toList
  def lastDigit = (l: List[Int]) => l(l.length - 1)
  def lastDigitFive = (x: Int) => if (lastDigit(listDigits(x)) == 5) true else false
  def sumDigitsDivByThree(x: Int): Boolean = {
    val digits = listDigits(x)
    var sum = 0
    digits.foreach(x => sum += x)
    sum % 3 == 0 && sum != 3
  }
}

class PrimeChecker {
  def isPrime(x: Int, l: List[Int]): Boolean = {
    var isP = true
    breakable {
      l.foreach(n => {
        if (x % n == 0){
          isP = false
          break
        }
      })
    }
    isP
  }

  def randomComposite(upto: Int): Int = {
    var x = Random.nextInt(upto - 1) + 1
    while (!Composite.isEven(x) && !Composite.lastDigitFive(x) && !Composite.sumDigitsDivByThree(x)) {
      x = Random.nextInt(upto)
    }
    x
  }

  def randomList(k: Int, upto: Int): List[Int] = {
    Seq.fill(k)({
      Random.nextInt(upto - 1) + 1
    }).toList
  }

  def randomComposites(k: Int, p: Int): List[Int] = {
    Seq.fill(k)(randomComposite(p)).toList
  }

  def simulate(k: Int): Unit = {
    var n = Random.nextInt(10000)
    val pr = if (isPrime(n, randomList(k, n))) "" else " not"
    println(s"The number: $n, is$pr prime.  Tested against $k numbers.")
  }

  def simVariableAccuracy(): Unit = {
    val acc = List(10, 100, 1000, 10000, 100000)
    val n = Random.nextInt(10000)
    acc.foreach(k => {
      val pr = if (isPrime(n, randomList(k, n))) "" else " not"
      println(s"The number: $n, is$pr prime.  Tested against $k numbers.")
    })
  }

  def simOnComposites(): Unit = {
    val acc = List(10, 100, 1000, 10000, 100000)
    val randComps = randomComposites(25, 250000)
    randComps.foreach( rc => {
      acc.foreach( a => {
        val pr = if (isPrime(rc, randomList(rc, a))) "was identified as prime" else "was identified as composite"
        println(s"The composite number: $rc, $pr.  Tested against $a numbers.")
      })
    })
  }
}

val pc = new PrimeChecker
pc.simVariableAccuracy
pc.simOnComposites
