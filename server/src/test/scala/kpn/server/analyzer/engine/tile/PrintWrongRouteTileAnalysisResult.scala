package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.analysis.caseStudies.CaseStudy
import kpn.server.analyzer.engine.tiles.TestTileSetup
import kpn.server.analyzer.engine.tiles.TileDataRouteBuilder
import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.Point

object PrintWrongRouteTileAnalysisResult {

  def main(args: Array[String]): Unit = {

    val routeAnalysis = CaseStudy.routeAnalysis("1029885")

    val tileDataRouteBuilder = new TileDataRouteBuilder(13)
    val tileRoute = tileDataRouteBuilder.build(routeAnalysis.route).get

    val t = new TestTileSetup()
    val analyzer = new RouteTileAnalyzerImpl(t.tileCalculator)
    val tiles = analyzer.tiles(13, tileRoute)

    val problemLines = Seq(
      Line(Point(5.9458731, 51.4545353), Point(5.9471328, 51.4535644)),
      Line(Point(5.9727521, 51.453642), Point(5.9721973, 51.455047)),
      Line(Point(5.9460367, 51.4541927), Point(5.9467283, 51.4536655))
    )

    val tileCalculator = new TileCalculatorImpl()
    val tile1 = tileCalculator.get("13-4231-2725")
    val tile2 = tileCalculator.get("13-4231-2726")

    val tileLines = Seq(
      tile1.bounds.left,
      tile1.bounds.right,
      tile1.bounds.top,
      tile1.bounds.bottom,
      tile2.bounds.left,
      tile2.bounds.right,
      tile2.bounds.top,
      tile2.bounds.bottom
    )

    val problemTile = tileCalculator.get("13-4231-2724")
    val problemTileLines = Seq(
      problemTile.bounds.left,
      problemTile.bounds.right,
      problemTile.bounds.top,
      problemTile.bounds.bottom
    )


    val allLines = tileRoute.segments.flatMap(_.lines) ++ tileLines ++ problemLines ++ problemTileLines

    val xs = allLines.flatMap(line => Seq(line.p1.x, line.p2.x))
    val ys = allLines.flatMap(line => Seq(line.p1.y, line.p2.y))


    val width = xs.max - xs.min
    val height = ys.max - ys.min

    val xfactor = 500 / width
    val yfactor = 1000 / height

    def printLine(line: Line, color: String): Unit = {
      val x1 = (line.p1.x - xs.min) * xfactor
      val y1 = (line.p1.y - ys.min) * yfactor
      val x2 = (line.p2.x - xs.min) * xfactor
      val y2 = (line.p2.y - ys.min) * yfactor
      println(s"""      <line x1="$x1" y1="$y1" x2="$x2" y2="$y2" stroke="$color" />""")
    }


    println("<html>")
    println("  <body>")
    println("""    <svg viewBox="0 0 1000 1000" version="1.1">""")

    val lines = tileRoute.segments.flatMap(_.lines)
    lines.foreach(line => printLine(line, "yellow"))
    tileLines.foreach(line => printLine(line, "black"))
    problemLines.foreach(line => printLine(line, "red"))
    problemTileLines.foreach(line => printLine(line, "green"))

    println("    </svg>")
    println("  </body>")
    println("</html>")
  }



}
