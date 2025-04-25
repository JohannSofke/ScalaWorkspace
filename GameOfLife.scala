import scala.annotation.tailrec

@main
def hello(): Unit =
  val initialState = gol.init()
  loop(initialState)

@tailrec
def loop(state: gol.Matrix[Boolean]): Unit =
  effects.clearScreen()
  effects.showWorld(gol.prepareToShow(state))
  effects.delay()
  
  val nextState = gol.loop(state) 
  loop(nextState)
