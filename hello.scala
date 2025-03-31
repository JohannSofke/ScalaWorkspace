@main
def hello(): Unit =
  init()
  while (true)
    loop()
    Thread.sleep(500)

def init(): Unit =
  println("Einmal")

import scala.util.Random

def loop(): Unit =
  println("\u001bc")
  println(Random.nextInt())
