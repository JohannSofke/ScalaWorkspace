package gol

import scala.util.Random

def init(): Unit =
  println("Einmal")

def loop(): Unit =
  clearScreen()
  println(Random.nextInt())

def delay(): Unit =
  Thread.sleep(500)

private def clearScreen(): Unit =
  println("\u001bc")
