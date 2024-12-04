package kpn.server.opendata.common

import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileUtil
import org.locationtech.jts.geom.Coordinate

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class OpenDataBitmapTileBuilder {

  def build(tile: Tile, nodes: Seq[OpenDataNode], routes: Seq[OpenDataRoute]): Array[Byte] = {
    val image = new BufferedImage(Tile.TILE_SIZE, Tile.TILE_SIZE, BufferedImage.TYPE_INT_ARGB)
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
    else {
      1f
    }

    val stroke = new BasicStroke(
      lineWidth,
      BasicStroke.CAP_ROUND,
      BasicStroke.JOIN_ROUND
    )

    val dash = Array[Float](1f)

    val virtualStroke = new BasicStroke(
      lineWidth,
      BasicStroke.CAP_BUTT,
      BasicStroke.JOIN_ROUND,
      0,
      dash,
      0
    )

    routes.foreach { tileRoute =>
      if (tileRoute.virtual) {
        g.setStroke(virtualStroke)
      }
      else {
        g.setStroke(stroke)
      }
      val worldCoordinates = tileRoute.coordinates.map(coordinate => new Coordinate(lonToWorldX(coordinate.lon), latToWorldY(coordinate.lat)))
      val tileCoordinates = TileUtil.tileCoordinates(tile, worldCoordinates)
      tileCoordinates.sliding(2).toSeq.foreach { case Seq(c1, c2) =>
        g.drawLine(c1.x, c1.y, c2.x, c2.y)
      }
    }
  }

  private def drawNodes(g: Graphics2D, tile: Tile, nodes: Seq[OpenDataNode]): Unit = {
    if (tile.z == 11) {
      nodes.foreach { node =>
        val worldCoordinate = new Coordinate(lonToWorldX(node.lon), latToWorldY(node.lat))
        val scaledCoordinate = tile.scale(worldCoordinate)
        val x = scaledCoordinate.x.toInt
        val y = scaledCoordinate.y.toInt
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
