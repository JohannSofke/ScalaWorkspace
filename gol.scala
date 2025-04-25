package gol

import scala.util.Random
import scala.compiletime.ops.int

type Matrix[T] = Vector[Vector[T]]

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

  val flatWorld = generateFlatWorld(20, 10)

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

def printFlatWorld(world: Matrix[Character]): Unit =
  val worldLinebreak = world.map(_ :+ Character.LineBreak)
  worldLinebreak.map(_.map(printCharacter(_)))

def generateFlatWorld(breite: Int, höhe: Int): Matrix[Character] =
  val flatWorld = Vector.fill(höhe, breite)(Random.nextInt(2) match
    case 0 => Character.Off
    case 1 => Character.On
  )
  flatWorld

def convolute(kernel: Matrix[Int], input: Matrix[Int]): Matrix[Int] =
  input
    .sliding(kernel.size)
    .map(
      _.transpose
        .sliding(kernel(0).size)
        .toVector
        .map(_.flatten.zip(kernel.flatten).map(_ * _).sum)
    )
    .toVector

def formDonutWorld[T](m: Matrix[T]): Matrix[T] =
  val mUpDown = m.last +: m :+ m.head
  val mLeftRight = mUpDown.map(row => row.last +: row :+ row.head)
  mLeftRight

// Testing packaged functions
import utest.*

private object GolTestSuite extends TestSuite:
  val tests = Tests:
    test("FlatWorld hat Größe von 10 x 4 Elementen"):
      val flatWorld = generateFlatWorld(10, 4)
      flatWorld.size ==> 4
      flatWorld(0).size ==> 10

    test("FlatWorld ist vom TypMatrix[Character]"):
      val flatWorld = generateFlatWorld(10, 4)
      flatWorld.isInstanceOf[Vector[Vector[Character]]] ==> true

    test("Bestimme die lebenden Nachbarn"):
      val flatWorld = Vector(
        Vector(1, 0, 1, 1, 0),
        Vector(1, 1, 0, 0, 1),
        Vector(0, 1, 0, 1, 0),
        Vector(0, 0, 1, 0, 1)
      )
      val kernel = Vector(
        Vector(1, 1, 1),
        Vector(1, 0, 1),
        Vector(1, 1, 1)
      )
      val ExepectedNeighbors = Vector(
        Vector(4, 5, 4),
        Vector(3, 4, 3)
      )
      val neighbors = convolute(kernel, flatWorld)
      neighbors ==> ExepectedNeighbors

    test("Forme eine Donut-Welt"):
      val flatWorld = Vector(
        Vector(1, 2, 3),
        Vector(4, 5, 6),
        Vector(7, 8, 9)
      )
      val expectedDonutWorld = Vector(
        Vector(9, 7, 8, 9, 7),
        Vector(3, 1, 2, 3, 1),
        Vector(6, 4, 5, 6, 4),
        Vector(9, 7, 8, 9, 7),
        Vector(3, 1, 2, 3, 1)
      )
      val donutWorld = formDonutWorld(flatWorld)
      donutWorld ==> expectedDonutWorld
