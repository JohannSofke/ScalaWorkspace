package gol

import scala.util.Random
import scala.compiletime.ops.int

def init(): Unit =
  println("Einmal")

def loop(): Unit =
  clearScreen()
  println(square(Random.nextInt(11)))

def delay(): Unit =
  Thread.sleep(500)

private def clearScreen(): Unit =
  println("\u001bc")

private def square(x: Int): Int =
  x * x
