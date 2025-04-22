package gol

import scala.util.Random
import scala.compiletime.ops.int

val HIDE_CURSOR = "\u001b[?25l"
val SHOW_CURSOR = "\u001b[?25h"

enum Character:
  case On, Off, Up, Down, LineBreak

def init(): Unit =
  scala.sys.addShutdownHook(
    print(SHOW_CURSOR)
  )
  print(HIDE_CURSOR)

def loop(): Unit =
  clearScreen()

  val flatWorld = generateFlatWorld(10, 4)

  printFlatWorld(flatWorld)

def delay(): Unit =
  Thread.sleep(500)

private def clearScreen(): Unit =
  println("\u001bc")

def printCharacter(c: Character): Unit =
  c match
    case Character.On        => print("█")
    case Character.Off       => print(" ")
    case Character.Up        => print("▀")
    case Character.Down      => print("▄")
    case Character.LineBreak => println()

def printFlatWorld(world: Vector[Vector[Character]]): Unit =
  val worldLinebreak = world.map(_ :+ Character.LineBreak)
  worldLinebreak.map(_.map(printCharacter(_)))

def generateFlatWorld(breite: Int, höhe: Int): Vector[Vector[Character]] =
  val flatWorld = Vector.fill(höhe, breite)(Random.nextInt(2) match
    case 0 => Character.Off
    case 1 => Character.On
  )
  flatWorld

private def square(x: Int): Int =
  x * x

// Testing packaged functions
import utest.*

private object GolTestSuite extends TestSuite:
  val tests = Tests:
    test("Quadriere Ganzzahl"):
      val x = square(10)
      x ==> 100

    test("FlatWorld hat Größe von 10 x 4 Elementen"):
      val flatWorld = generateFlatWorld(10, 4)
      flatWorld.size ==> 4
      flatWorld(0).size ==> 10

    test("FlatWorld ist vom Typ Vector[Vector[Character]]"):
      val flatWorld = generateFlatWorld(10, 4)
      flatWorld.isInstanceOf[Vector[Vector[Character]]] ==> true
