@main
def hello(): Unit =
  gol.init()

  while (true)
    gol.loop()
    gol.delay()
