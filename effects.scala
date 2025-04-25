package effects

def delay(): Unit =
  Thread.sleep(500)

def clearScreen(): Unit =
  println("\u001bc")

def showWorld(world: gol.Matrix[Boolean]): Unit =
  val worldCharacter = world.map(_.map(gol.characterFromBoolean(_)))
  val worldLinebreak = worldCharacter.map(_ :+ gol.Character.LineBreak)
  worldLinebreak.map(_.map(printCharacter(_)))

private def printCharacter(c: gol.Character): Unit =
  c match
    case gol.Character.On        => print("█")
    case gol.Character.Off       => print(" ")
    case gol.Character.Up        => print("▀")
    case gol.Character.Down      => print("▄")
    case gol.Character.LineBreak => println()
