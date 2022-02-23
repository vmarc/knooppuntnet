package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.analysis.caseStudies.CaseStudy
import kpn.server.analyzer.engine.tiles.TileDataRouteBuilder
import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.Point

object WrongRouteTileAnalysisPrinter {
  def main(args: Array[String]): Unit = {
    new WrongRouteTileAnalysisPrinter().print()
  }
}

class WrongRouteTileAnalysisPrinter {

  private val tileCalculator = new TileCalculatorImpl()

  def print(): Unit = {

    val routeLines = {
      val routeAnalysis = CaseStudy.routeAnalysis("1029885")
      val tileDataRouteBuilder = new TileDataRouteBuilder(13)
      val tileRoute = tileDataRouteBuilder.fromRouteInfo(routeAnalysis.toRouteTileInfo)
      tileRoute.segments.flatMap(_.lines)
    }

    val problemLines = Seq(
      Line(Point(5.9458731, 51.4545353), Point(5.9471328, 51.4535644)),
      Line(Point(5.9727521, 51.453642), Point(5.9721973, 51.455047)),
      Line(Point(5.9460367, 51.4541927), Point(5.9467283, 51.4536655))
    )

    val routeTileLines = linesInTile("13-4231-2725") ++ linesInTile("13-4231-2726")
    val problemTileLines = linesInTile("13-4231-2724")

    val allLines = routeLines ++ routeTileLines ++ problemLines ++ problemTileLines

    val xs = allLines.flatMap(line => Seq(line.p1.x, line.p2.x))
    val ys = allLines.flatMap(line => Seq(line.p1.y, line.p2.y))

    val width = xs.max - xs.min
    val height = ys.max - ys.min

    val xfactor = 350 / width
    val yfactor = 1000 / height

    def printLines(lines: Seq[Line], color: String): Unit = {
      lines.foreach { line =>
        val x1 = Math.round((line.p1.x - xs.min) * xfactor) + 1
        val y1 = Math.round(1000 - (line.p1.y - ys.min) * yfactor) + 1
        val x2 = Math.round((line.p2.x - xs.min) * xfactor) + 1
        val y2 = Math.round(1000 - (line.p2.y - ys.min) * yfactor) + 1
        println(s"""      <line x1="$x1" y1="$y1" x2="$x2" y2="$y2" stroke="$color" stroke-width="2"/>""")
      }
    }

    printHeader()
    printLines(routeLines, "yellow")
    printLines(routeTileLines, "black")
    printLines(problemLines, "red")
    printLines(problemTileLines, "green")
    printFooter()
  }

  private def linesInTile(tileName: String): Seq[Line] = {
    val tile = tileCalculator.tileNamed(tileName)
    Seq(
      tile.bounds.left,
      tile.bounds.right,
      tile.bounds.top,
      tile.bounds.bottom
    )
  }

  private def printHeader(): Unit = {
    println("<html>")
    println("  <body>")
    println("""    <svg viewBox="0 0 1010 1010" version="1.1">""")
  }

  private def printFooter(): Unit = {
    println("    </svg>")
    println("  </body>")
    println("</html>")
  }

}
