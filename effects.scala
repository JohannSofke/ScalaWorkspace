package effects

def delay(milliseconds: Int): Unit =
  Thread.sleep(milliseconds)

def clearScreen(): Unit =
  println("\u001bc")

def showWorld(world: String): Unit =
  print(world)
