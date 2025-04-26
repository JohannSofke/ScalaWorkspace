package effects

def delay(): Unit =
  Thread.sleep(200)

def clearScreen(): Unit =
  println("\u001bc")

def showWorld(world: String): Unit =
  print(world)
