package gol

import scala.util.Random
import scala.compiletime.ops.int

type Matrix[T] = Vector[Vector[T]]

enum Character:
  case On, Off, Up, Down, LineBreak

val CURSOR_HIDE = "\u001b[?25l"
val CURSOR_SHOW = "\u001b[?25h"
val DMINENSION = 50

def init(): Matrix[Boolean] =
  scala.sys.addShutdownHook(
    print(CURSOR_SHOW)
  )
  print(CURSOR_HIDE)

  val flatWorld = generateFlatWorld(DMINENSION, DMINENSION)
  flatWorld

def loop(flatWorld: Matrix[Boolean]): Matrix[Boolean] =
  val donutWorld = formDonutWorld(flatWorld)
  val neighbors = countNeighbors(donutWorld)
  val newFlatWorld = updateFlatWorld(flatWorld, neighbors)
  newFlatWorld

def prepareToShow(flatWorld: Matrix[Boolean]): Matrix[Character] =
  val (evenRows, oddRows) = splitEvenOddRows(flatWorld)
  val combinedCharacter = combineEvenOddRows(evenRows, oddRows)
  val worldLinebreak = combinedCharacter.map(_ :+ Character.LineBreak)
  worldLinebreak

private def generateFlatWorld(breite: Int, höhe: Int): Matrix[Boolean] =
  val flatWorld = Vector.fill(höhe, breite)(
    Random.nextInt(2) match
      case 0 => false
      case 1 => true
  )
  flatWorld

private def countNeighbors(donutWorld: Matrix[Boolean]): Matrix[Int] =
  val kernel = Vector(
    Vector(1, 1, 1),
    Vector(1, 0, 1),
    Vector(1, 1, 1)
  )
  val neighbors = convolute(kernel, donutWorld)
  neighbors

private def convolute(kernel: Matrix[Int], input: Matrix[Boolean]): Matrix[Int] =
  implicit def bool2int(b: Boolean): Int = if b then 1 else 0

  val convolutionMatrix = input
    .sliding(kernel.size)
    .map(
      _.transpose
        .sliding(kernel.head.size)
        .toVector
        .map(_.flatten.zip(kernel.flatten).map(_ * _).sum)
    )
    .toVector
  convolutionMatrix

private def formDonutWorld[T](m: Matrix[T]): Matrix[T] =
  val mUpDown = m.last +: m :+ m.head
  val mLeftRight = mUpDown.map(row => row.last +: row :+ row.head)
  mLeftRight

private def updateFlatWorld(
    flatWorld: Matrix[Boolean],
    neighbors: Matrix[Int]
): Matrix[Boolean] =
  val newFlatWorld = flatWorld
    .zip(neighbors)
    .map: (rowsFW, rowsN) =>
      rowsFW
        .zip(rowsN)
        .map: (cellsFW, cellsN) =>
          (cellsFW, cellsN) match
            case (true, 2 | 3) => true
            case (false, 3)    => true
            case _             => false
  newFlatWorld

private def splitEvenOddRows[T](matrix: Matrix[T]): (Matrix[T], Matrix[T]) =
  val evenRows = matrix.zipWithIndex.collect:
    case (row, index) if index % 2 == 0 => row
  val oddRows = matrix.zipWithIndex.collect:
    case (row, index) if index % 2 != 0 => row
  (evenRows, oddRows)

private def combineEvenOddRows(
    evenRows: Matrix[Boolean],
    oddRows: Matrix[Boolean]
): Matrix[Character] =
  val combineMatrix = evenRows
    .zip(oddRows)
    .map: (evenRow, oddRow) =>
      evenRow
        .zip(oddRow)
        .map: (evenCell, oddCell) =>
          (evenCell, oddCell) match
            case (true, true)   => Character.On
            case (true, false)  => Character.Up
            case (false, true)  => Character.Down
            case (false, false) => Character.Off
  combineMatrix

// Testing packaged functions
import utest.*

private object GolTestSuite extends TestSuite:
  val tests = Tests:
    test("FlatWorld hat Größe von 10 x 4 Elementen"):
      val flatWorld = generateFlatWorld(10, 4)
      flatWorld.size ==> 4
      flatWorld.head.size ==> 10

    test("FlatWorld ist vom Typ Matrix[Boolean]"):
      val flatWorld = generateFlatWorld(10, 4)
      flatWorld.isInstanceOf[Matrix[Boolean]] ==> true

    test("Bestimme die lebenden Nachbarn"):
      val flatWorld = Vector(
        Vector(true, false, true, true, false),
        Vector(true, true, false, false, true),
        Vector(false, true, false, true, false),
        Vector(false, false, true, false, true)
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

      test("Spielregeln"):
        val flatWorld = Vector(
          Vector(true, true),
          Vector(true, false)
        )
        val neighbors = Vector(
          Vector(1, 2),
          Vector(4, 3)
        )
        val expectedNewFlatWorld = Vector(
          Vector(false, true),
          Vector(false, true)
        )
        updateFlatWorld(flatWorld, neighbors) ==> expectedNewFlatWorld

      test("Teile die Matrix in gerade und ungerade Zeilen"):
        val matrix = Vector(
          Vector(1, 2, 3),
          Vector(4, 5, 6),
          Vector(7, 8, 9),
          Vector(10, 11, 12)
        )
        val (evenRows, oddRows) = splitEvenOddRows(matrix)
        evenRows ==> Vector(
          Vector(1, 2, 3),
          Vector(7, 8, 9)
        )
        oddRows ==> Vector(
          Vector(4, 5, 6),
          Vector(10, 11, 12)
        )

      test("Kombiniere gerade und ungerade Zeilen zu einer Charakter Matrix"):
        val evenRows = Vector(
          Vector(true, true),
          Vector(false, false)
        )
        val oddRows = Vector(
          Vector(true, false),
          Vector(false, true)
        )
        val expectedCombinedMatrix = Vector(
          Vector(Character.On, Character.Up),
          Vector(Character.Off, Character.Down)
        )
        combineEvenOddRows(evenRows, oddRows) ==> expectedCombinedMatrix

      test("Bereite die Matrix für die Anzeige vor"):
        val flatWorld = Vector(
          Vector(true, true, false, false),
          Vector(true, false, true, false)
        )
        val expectedPreparedMatrix = Vector(
          Vector(
            Character.On,
            Character.Up,
            Character.Down,
            Character.Off,
            Character.LineBreak
          )
        )
        prepareToShow(flatWorld) ==> expectedPreparedMatrix
