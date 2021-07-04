package kpn.server.analyzer.engine.tiles.raster

import kpn.server.analyzer.engine.tiles.{TileBuilder, TileData}
import kpn.server.analyzer.engine.tiles.domain.Tile

import java.awt.{BasicStroke, Color, Font, Graphics2D, RenderingHints}
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class RasterTileBuilder(tileColor: TileColor) extends TileBuilder {

  private val width = 256
  private val height = 256

  def build(data: TileData): Array[Byte] = {
    val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val g = createGraphics(image)
    try {
      drawRoutes(g, data)
      drawNodes(g, data)
    }
    finally {
      g.dispose()
    }
    toByteArray(image)
  }

  private def drawRoutes(g: Graphics2D, data: TileData): Unit = {

    val lineWidth = if (data.tile.z < 9) {
      0.5f
    }
    else if (data.tile.z < 10) {
      1f
    }
    else {
      2f
    }

    val standardStroke = new BasicStroke(
      lineWidth,
      BasicStroke.CAP_ROUND,
      BasicStroke.JOIN_ROUND
    )

    val proposedStroke = new BasicStroke(
      lineWidth,
      BasicStroke.CAP_BUTT,
      BasicStroke.JOIN_ROUND,
      5f,
      Array(6f, 10f),
      0f
    )

    data.routes.foreach { tileRoute =>
      val stroke = tileRoute.state match {
        case Some("proposed") => proposedStroke
        case _ => standardStroke
      }
      g.setStroke(stroke)
      tileRoute.segments.foreach { segment =>
        g.setColor(tileColor.routeColor(tileRoute, segment))
        segment.lines.foreach { line =>
          val x1 = lngToPixel(data.tile, line.p1.x)
          val y1 = latToPixel(data.tile, line.p1.y)
          val x2 = lngToPixel(data.tile, line.p2.x)
          val y2 = latToPixel(data.tile, line.p2.y)
          g.drawLine(x1, y1, x2, y2)
        }
      }
    }
  }

  private def lngToPixel(tile: Tile, lng: Double): Int = {
    ((lng - tile.bounds.xMin) * width / (tile.bounds.xMax - tile.bounds.xMin)).round.toInt
  }

  private def latToPixel(tile: Tile, lat: Double): Int = {
    (height - ((lat - tile.bounds.yMin) * height / (tile.bounds.yMax - tile.bounds.yMin))).round.toInt
  }

  private def drawNodes(g: Graphics2D, data: TileData): Unit = {

    data.nodes.foreach { node =>

      g.setColor(tileColor.nodeColor(node))

      val x = lngToPixel(data.tile, node.lon)
      val y = latToPixel(data.tile, node.lat)

      if (data.tile.z == 10) {
        g.fillOval(x - 1, y - 1, 3, 3)
      }
      else if (data.tile.z > 10) {
        g.fillOval(x - 1, y - 1, 3, 3)
      }
    }
  }

  private def createGraphics(image: BufferedImage) = {
    val g = image.createGraphics()
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    g.setFont(new Font("Arial", Font.PLAIN, 10))
    g
  }

  private def toByteArray(image: BufferedImage) = {
    val out = new ByteArrayOutputStream()
    ImageIO.write(image, "png", out)
    out.close()
    out.toByteArray
  }

}
