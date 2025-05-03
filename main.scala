import scala.annotation.tailrec

@main
def main(seed: Int): Unit =
  effects.initTerminal()
  val initialState = gol.init(seed)
  loop(initialState)

@tailrec
def loop(state: gol.Matrix[Boolean]): Unit =
  effects.resetTerminal()
  effects.showWorld(gol.prepareToShow(state))
  effects.delay(200)

  val nextState = gol.loop(state)
  loop(nextState)
