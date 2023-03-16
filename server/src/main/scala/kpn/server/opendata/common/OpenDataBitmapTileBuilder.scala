package kpn.server.opendata.common

import kpn.server.analyzer.engine.tiles.domain.Tile

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class OpenDataBitmapTileBuilder {

  private val width = 256
  private val height = 256

  def build(tile: Tile, nodes: Seq[OpenDataNode], routes: Seq[OpenDataRoute]): Array[Byte] = {
    val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val g = createGraphics(image)
    g.setColor(Color.red)
    try {
      drawRoutes(g, tile, routes)
      drawNodes(g, tile, nodes)
    }
    finally {
      g.dispose()
    }
    toByteArray(image)
  }

  private def drawRoutes(g: Graphics2D, tile: Tile, routes: Seq[OpenDataRoute]): Unit = {

    val lineWidth = if (tile.z < 9) {
      0.5f
    }
    else if (tile.z < 10) {
      1f
    }
    else {
      2f
    }

    val stroke = new BasicStroke(
      lineWidth,
      BasicStroke.CAP_ROUND,
      BasicStroke.JOIN_ROUND
    )

    routes.foreach { tileRoute =>
      g.setStroke(stroke)
      tileRoute.coordinates.sliding(2).toSeq.foreach { case Seq(p1, p2) =>
        val x1 = tile.lngToPixel(width, p1.lon)
        val y1 = tile.latToPixel(height, p1.lat)
        val x2 = tile.lngToPixel(width, p2.lon)
        val y2 = tile.latToPixel(height, p2.lat)
        g.drawLine(x1, y1, x2, y2)
      }
    }
  }

  private def drawNodes(g: Graphics2D, tile: Tile, nodes: Seq[OpenDataNode]): Unit = {

    nodes.foreach { node =>

      val x = tile.lngToPixel(width, node.lon)
      val y = tile.latToPixel(height, node.lat)

      if (tile.z == 10) {
        g.fillOval(x - 1, y - 1, 3, 3)
      }
      else if (tile.z > 10) {
        g.fillOval(x - 1, y - 1, 3, 3)
      }
    }
  }

  private def createGraphics(image: BufferedImage): Graphics2D = {
    val g = image.createGraphics()
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    g
  }

  private def toByteArray(image: BufferedImage): Array[Byte] = {
    val out = new ByteArrayOutputStream()
    ImageIO.write(image, "png", out)
    out.close()
    out.toByteArray
  }
}
