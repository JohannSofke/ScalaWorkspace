package effects

def initTerminal(): Unit =
  scala.sys.addShutdownHook(
    shutdownHook()
  )
  hideCursor()
  activateAlternativeScreenBuffer()

def shutdownHook(): Unit =
  deactivateAlternativeScreenBuffer()
  showCursor()

def delay(milliseconds: Int): Unit =
  Thread.sleep(milliseconds)

def resetCursor(): Unit =
  print("\u001b[H")

def showCursor(): Unit =
  print("\u001b[?25h")

def hideCursor(): Unit =
  print("\u001b[?25l")

def showWorld(world: String): Unit =
  println(world)

def activateAlternativeScreenBuffer(): Unit =
  print("\u001b[?1049h")

def deactivateAlternativeScreenBuffer(): Unit =
  print("\u001b[?1049l")