package gol

import scala.util.Random
import scala.compiletime.ops.int

enum Character:
  case On, Off, Up, Down

def init(): Unit =
  println("Einmal")

def loop(): Unit =
  clearScreen()
  printCharacter(Character.On)
  printCharacter(Character.Off)
  printCharacter(Character.Up)
  printCharacter(Character.Down)

def delay(): Unit =
  Thread.sleep(500)

private def clearScreen(): Unit =
  println("\u001bc")

def printCharacter(c: Character): Unit =
  c match
    case Character.On   => print("█")
    case Character.Off  => print(" ")
    case Character.Up   => print("▀")
    case Character.Down => print("▄")

private def square(x: Int): Int =
  x * x

// Testing packaged functions
import utest.*

private object GolTestSuite extends TestSuite:
  val tests = Tests:
    test("Quadriere Ganzzahl"):
      val x = square(10)
      x ==> 100
