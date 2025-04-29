package effects

def delay(milliseconds: Int): Unit =
  Thread.sleep(milliseconds)

def resetTerminal(): Unit =
  print("\u001bc")
  hideCursor()

def showCursor(): Unit =
  print("\u001b[?25h")

def hideCursor(): Unit =
  print("\u001b[?25l")

def showWorld(world: String): Unit =
  println(world)
