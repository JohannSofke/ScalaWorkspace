package gol

import scala.util.Random
import scala.compiletime.ops.int

type Matrix[T] = Vector[Vector[T]]

enum Character:
  case On, Off, Up, Down, LineBreak

val HIDE_CURSOR = "\u001b[?25l"
val SHOW_CURSOR = "\u001b[?25h"

def init(): Matrix[Boolean] =
  scala.sys.addShutdownHook(
    print(SHOW_CURSOR)
  )
  print(HIDE_CURSOR)

  val flatWorld = generateFlatWorld(50, 50)
  flatWorld

def loop(flatWorld: Matrix[Boolean]): Matrix[Boolean] =
  val donutWorld = formDonutWorld(flatWorld)
  val neighbors = countNeighbors(donutWorld)
  val newFlatWorld = updateFlatWorld(flatWorld, neighbors)
  newFlatWorld

def characterFromBoolean(b: Boolean): Character = 
  b match
    case true  => Character.On
    case false => Character.Off

def generateFlatWorld(breite: Int, höhe: Int): Matrix[Boolean] =
  val flatWorld = Vector.fill(höhe, breite)(Random.nextInt(2) match
    case 0 => false
    case 1 => true
  )
  flatWorld

def countNeighbors(donutWorld: Matrix[Boolean]): Matrix[Int] =
  val kernel = Vector(
    Vector(1, 1, 1),
    Vector(1, 0, 1),
    Vector(1, 1, 1)
  )
  convolute(kernel, donutWorld)

def convolute(kernel: Matrix[Int], input: Matrix[Boolean]): Matrix[Int] =
  implicit def bool2int(b: Boolean): Int = if b then 1 else 0

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

def updateFlatWorld(flatWorld: Matrix[Boolean], neighbors: Matrix[Int]): Matrix[Boolean] =
  flatWorld.zip(neighbors).map: (rowFW, rowN) =>
    rowFW.zip(rowN).map: (cellFW, cellN) =>
      if cellFW && (cellN == 2 || cellN == 3) then true
      else if !cellFW && cellN == 3 then true
      else false

def prepareToShow(flatWorld: Matrix[Boolean]): Matrix[Character] =
  val (evenRows, oddRows) = splitEvenOddRows(flatWorld)
  val combinedCharacter = combineEvenOddRows(evenRows, oddRows)
  val worldLinebreak = combinedCharacter.map(_ :+ Character.LineBreak)
  worldLinebreak 

def splitEvenOddRows[T](matrix: Matrix[T]): (Matrix[T], Matrix[T]) =
  val evenRows = matrix.zipWithIndex.collect { case (row, index) if index % 2 == 0 => row }
  val oddRows = matrix.zipWithIndex.collect { case (row, index) if index % 2 != 0 => row }
  (evenRows, oddRows)

def combineEvenOddRows(evenRows: Matrix[Boolean], oddRows: Matrix[Boolean]): Matrix[Character] =
  evenRows.zip(oddRows).map: (rowE, rowO) =>
    rowE.zip(rowO).map: (cellE, cellO) =>
      (cellE, cellO) match
        case (true, true) => Character.On
        case (true, false) => Character.Up
        case (false, true) => Character.Down
        case (false, false) => Character.Off
      
// Testing packaged functions
import utest.*

private object GolTestSuite extends TestSuite:
  val tests = Tests:
    test("FlatWorld hat Größe von 10 x 4 Elementen"):
      val flatWorld = generateFlatWorld(10, 4)
      flatWorld.size ==> 4
      flatWorld(0).size ==> 10

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

      test("Konvertiere Boolean in Character"):
        characterFromBoolean(true) ==> Character.On
        characterFromBoolean(false) ==> Character.Off

      test("Spielregeln"):
        val flatWorld = Vector(
          Vector(true, true),
          Vector(true, false)
        )
        val neighbors = Vector(
          Vector(1, 2),
          Vector(4, 3),
        )
        val expectedNewFlatWorld = Vector(
          Vector(false, true),
          Vector(false, true),
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
          Vector(true, false, true, false),
        )
        val expectedPreparedMatrix = Vector(
          Vector(Character.On, Character.Up, Character.Down, Character.Off, Character.LineBreak),
        )
        prepareToShow(flatWorld) ==> expectedPreparedMatrix
