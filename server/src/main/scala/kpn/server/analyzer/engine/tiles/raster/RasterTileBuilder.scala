package kpn.server.analyzer.engine.tiles.raster

import kpn.server.analyzer.engine.tiles.TileBuilder
import kpn.server.analyzer.engine.tiles.TileData
import kpn.server.analyzer.engine.tiles.domain.Tile
import org.locationtech.jts.geom.Coordinate

import java.awt.BasicStroke
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class RasterTileBuilder(tileColor: TileColor) extends TileBuilder {

  private val width = 256
  private val height = 256

  def build(data: TileData, tile: Tile): Array[Byte] = {
    val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val g = createGraphics(image)
    try {
      drawRoutes(g, data, tile)
      drawNodes(g, data, tile)
    }
    finally {
      g.dispose()
    }
    toByteArray(image)
  }

  private def drawRoutes(g: Graphics2D, data: TileData, tile: Tile): Unit = {

    val lineWidth = if (tile.z < 9) {
      0.5f
    }
    else if (tile.z < 10) {
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
        val worldCoordinates = segment.worldCoordinates.sliding(2, 2).toSeq.map { case Seq(x, y) => new Coordinate(x, y) }
        worldCoordinates.sliding(2).toSeq.map { case Seq(p0, p1) =>
          val x1 = lngToPixel(tile, p0.x)
          val y1 = latToPixel(tile, p0.y)
          val x2 = lngToPixel(tile, p1.x)
          val y2 = latToPixel(tile, p1.y)
          g.drawLine(x1, y1, x2, y2)
        }
      }
    }
  }

  private def lngToPixel(tile: Tile, lng: Double): Int = {
    ((lng - tile.bounds.xMin) * width / (tile.bounds.xMax - tile.bounds.xMin)).round.toInt
  }

  private def latToPixel(tile: Tile, lat: Double): Int = {
    ((lat - tile.bounds.yMin) * height / (tile.bounds.yMax - tile.bounds.yMin)).round.toInt
  }

  private def drawNodes(g: Graphics2D, data: TileData, tile: Tile): Unit = {

    data.nodes.foreach { node =>

      g.setColor(tileColor.nodeColor(node))

      val x = lngToPixel(tile, node.lon)
      val y = latToPixel(tile, node.lat)

      if (tile.z == 10) {
        g.fillOval(x - 1, y - 1, 3, 3)
      }
      else if (tile.z > 10) {
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
