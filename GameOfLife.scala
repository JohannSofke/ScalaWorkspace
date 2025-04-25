import scala.annotation.tailrec

@main
def hello(): Unit =
  val initialState = gol.init()
  loop(initialState)

@tailrec
def loop(state: Vector[Vector[Boolean]]): Unit =
  effects.clearScreen()
  effects.showWorld(state)
  effects.delay()
  
  val nextState = gol.loop(state) 
  loop(nextState)
