import scala.annotation.tailrec

@main
def hello(): Unit =
  val initialState = gol.init()
  loop(initialState)

@tailrec
def loop(state: Vector[Vector[Boolean]]): Unit =
  val nextState = gol.loop(state)
  gol.delay()
  loop(nextState)
