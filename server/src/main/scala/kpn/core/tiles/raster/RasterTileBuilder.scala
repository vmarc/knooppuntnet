package kpn.core.tiles.raster

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream

import javax.imageio.ImageIO
import kpn.api.common.network.NetworkNodeInfo2
import kpn.core.tiles.TileBuilder
import kpn.core.tiles.TileData
import kpn.core.tiles.domain.Tile
import kpn.core.tiles.domain.TileRoute

class RasterTileBuilder extends TileBuilder {

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

    val lineWidth = if (data.tile.z < 8) {
      0.5f
    }
    else {
      1f
    }

    g.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))

    data.routes.foreach { tileRoute =>

      g.setColor(routeColor(tileRoute))

      tileRoute.segments.foreach { segment =>
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

  private def drawNodes(g: Graphics2D, data: TileData): Unit = {

    data.nodes.foreach { node =>

      g.setColor(nodeColor(node))

      val x = lngToPixel(data.tile, node.lon)
      val y = latToPixel(data.tile, node.lat)

      if (data.tile.z == 10) {
        g.fillOval(x - 2, y - 2, 5, 5)
      }
      else if (data.tile.z > 10) {
        g.fillOval(x - 3, y - 3, 7, 7)
      }
    }
  }

  private def lngToPixel(tile: Tile, lng: Double): Int = {
    ((lng - tile.bounds.xMin) * width / (tile.bounds.xMax - tile.bounds.xMin)).round.toInt
  }

  private def latToPixel(tile: Tile, lat: Double): Int = {
    (height - ((lat - tile.bounds.yMin) * height / (tile.bounds.yMax - tile.bounds.yMin))).round.toInt
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

  private def routeColor(tileRoute: TileRoute): Color = {
    val colorValue = tileRoute.layer match {
      case "orphan-route" => "#006000" // MainStyleColors.darkGreen
      case "incomplete-route" => "#ff0000" // MainStyleColors.red
      case "error-route" => "#ffa500" // orange
      case "route" => "#00c800" // MainStyleColors.green
      case _ => "#00c800" // MainStyleColors.green
    }
    Color.decode(colorValue)
  }

  private def nodeColor(node: NetworkNodeInfo2): Color = {

    // TODO MAP share logic with VectorTileBuilder
    val colorString = if (!node.definedInRelation && !node.definedInRelation && node.routeReferences.isEmpty) {
      if (node.integrityCheck.isDefined && node.integrityCheck.get.failed) {
        "#0000bb" // MainStyleColors.darkBlue
      }
      else {
        "#006000" // MainStyleColors.darkGreen
      }
    }
    else if (node.integrityCheck.isDefined && node.integrityCheck.get.failed) {
      "#0000ff" // MainStyleColors.blue
    }
    else {
      "#00c800" // MainStyleColors.green
    }

    Color.decode(colorString)
  }

}
