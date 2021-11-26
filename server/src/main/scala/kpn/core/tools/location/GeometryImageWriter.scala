package kpn.core.tools.location

import org.apache.commons.io.FileUtils
import org.locationtech.jts.awt.PointTransformation
import org.locationtech.jts.awt.ShapeWriter
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Envelope
import org.locationtech.jts.geom.Geometry

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.Point2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

class ScaleTransformation(width: Int, height: Int, envelope: Envelope) extends PointTransformation {

  private val xScale = 0.66 // Math.abs(Math.cos((envelope.getMinY + envelope.getMaxY) / 2))
  private val xMin = envelope.getMinX * xScale
  private val yMin = envelope.getMinY
  private val envelopeWidth = (envelope.getMaxX * xScale) - (envelope.getMinX * xScale)
  private val envelopeHeight = envelope.getMaxY - envelope.getMinY
  private val envelopeScale = if (envelopeWidth > envelopeHeight) {
    envelopeWidth
  }
  else {
    envelopeHeight
  }

  override def transform(coordinate: Coordinate, point2D: Point2D): Unit = {
    val x = (coordinate.x * xScale - xMin) * width / envelopeScale
    val y = height - ((coordinate.y - yMin) * height / envelopeScale)
    point2D.setLocation(x, y)
  }
}

class GeometryImageWriter(width: Int, height: Int, envelope: Envelope) {

  private val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
  val g = createGraphics(image)
  private val pointTransformation: PointTransformation = new ScaleTransformation(width, height, envelope)
  val shapeWriter = new ShapeWriter(pointTransformation)

  def write(filename: String): Unit = {
    val out = new ByteArrayOutputStream()
    ImageIO.write(image, "png", out)
    out.close()
    FileUtils.writeByteArrayToFile(new File(filename), out.toByteArray)
  }

  def draw(geometry: Geometry, color: Color, width: Float): Unit = {
    val shape = shapeWriter.toShape(geometry)
    g.setColor(color)
    g.setStroke(new BasicStroke(width))
    g.draw(shape)
  }

  def fill(geometry: Geometry, color: Color): Unit = {
    val shape = shapeWriter.toShape(geometry)
    g.setColor(color)
    g.fill(shape)
  }

  private def createGraphics(image: BufferedImage): Graphics2D = {
    val g = image.createGraphics()
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    g.setFont(new Font("Arial", Font.PLAIN, 10))
    g.setColor(Color.white)
    g.fillRect(0, 0, width, height)
    g
  }
}
