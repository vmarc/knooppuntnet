package kpn.core.report

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Image
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream

import javax.imageio.ImageIO
import kpn.core.analysis.Link
import kpn.core.analysis.LinkType

object LinkImageBuilder {

  val size = 40

  val corners: Image = readImage("roundedcorners.png")
  val roundabout: Image = readImage("roundabout_right.png")

  private def readImage(name: String): Image = {
    val file = new File("/home/marcv/git/projects/gps/images/" + name)
    ImageIO.read(file)
  }

  def build(fileName: String, link: Link): Unit = {
    val image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    val g2 = image.createGraphics()
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

    g2.setColor(Color.white)
    g2.fillRect(0, 0, size, size)
    new LinkImageBuilder(g2, link).paint()
    val out = new FileOutputStream(fileName)
    ImageIO.write(image, "png", out)
    out.close()
  }
}

class LinkImageBuilder(g: Graphics2D, link: Link) {

  import kpn.core.report.LinkImageBuilder._

  def paint(): Unit = {

    if (link == null || !link.isValid) {
      paintNode()
    }
    else {
      paintWay()
    }
  }

  private def paintWay(): Unit = {
    val width = size
    val height = size

    val ymax = height - 1
    val xloop = 14

    val xowloop = if (link.isOnewayLoopBackwardPart) {
      7
    }
    else if (link.isOnewayLoopForwardPart) {
      -7
    }
    else {
      0
    }

    val xoff = if (link.isLoop) {
      width / 2 - (xloop / 2 - 1)
    }
    else {
      width / 2
    }

    val w = 4
    val p = 4 + w + 1
    var y1 = 0
    var y2 = 0

    if (!link.hasPrev) {
      if (link.isLoop) {
        g.setColor(Color.black)
        y1 = 5
        g.drawImage(corners, xoff, y1 - 3, xoff + 3, y1, 0, 0, 3, 3, new Color(0, 0, 0, 0), null)
        g.drawImage(corners, xoff + xloop - 2, y1 - 3, xoff + xloop + 1, y1, 2, 0, 5, 3, new Color(0, 0, 0, 0), null)
        g.drawLine(xoff + 3, y1 - 3, xoff + xloop - 3, y1 - 3)
      }
      else {
        g.setColor(Color.red)
        if (link.isOnewayHead) {
          g.drawRect(xoff - 2, p - 3 - w, w, w)
          g.fillRect(xoff - 2, p - 3 - w, w, w)
        } else {
          g.drawRect(xoff - 2 + xowloop, p - 1 - w, w, w)
          g.fillRect(xoff - 2 + xowloop, p - 1 - w, w, w)
        }
        y1 = p
      }
    }

    if (link.hasNext) {
      y2 = ymax
    } else {
      if (link.isLoop) {
        g.setColor(Color.black)
        y2 = ymax - 5
        g.fillRect(xoff - 1, y2 + 2, 3, 3)
        g.drawLine(xoff, y2, xoff, y2 + 2)
        g.drawImage(corners, xoff + xloop - 2, y2 + 1, xoff + xloop + 1, y2 + 4, 2, 2, 5, 5, new Color(0, 0, 0, 0), null)
        g.drawLine(xoff + 3 - 1, y2 + 3, xoff + xloop - 3, y2 + 3)
      }
      else {
        g.setColor(Color.red)
        if (link.isOnewayTail) {
          g.drawRect(xoff - 2, ymax - p + 3, w, w)
          g.fillRect(xoff - 2, ymax - p + 3, w, w)
        } else {
          g.drawRect(xoff - 2 + xowloop, ymax - p + 1, w, w)
          g.fillRect(xoff - 2 + xowloop, ymax - p + 1, w, w)
        }
        y2 = ymax - p
      }
    }

    /* vertical lines */
    g.setColor(Color.blue)
    //g.setStroke(new BasicStroke(3f))
    if (link.isLoop) {
      g.drawLine(xoff + xloop, y1, xoff + xloop, y2)
    }

    if (link.isOnewayHead) {
      setDotted(g)
      y1 = 7

      val xValues = Array(xoff - xowloop + 1, xoff - xowloop + 1, xoff)
      val yValues = Array(ymax, y1 + 1, 1)
      g.drawPolyline(xValues, yValues, 3)
      unsetDotted(g)
      g.drawLine(xoff + xowloop, y1 + 1, xoff, 1)
    }

    if (link.isOnewayTail) {
      setDotted(g)
      y2 = ymax - 7

      val xValues = Array(xoff + 1, xoff - xowloop + 1, xoff - xowloop + 1)
      val yValues = Array(ymax - 1, y2, y1)
      g.drawPolyline(xValues, yValues, 3)
      unsetDotted(g)
      g.drawLine(xoff + xowloop, y2, xoff, ymax - 1)
    }

    if ((link.isOnewayLoopForwardPart || link.isOnewayLoopBackwardPart) && !link.isOnewayTail && !link.isOnewayHead) {
      setDotted(g)
      g.drawLine(xoff - xowloop + 1, y1, xoff - xowloop + 1, y2 + 1)
      unsetDotted(g)
    }

    if (!link.isOnewayLoopForwardPart && !link.isOnewayLoopBackwardPart) {
      g.drawLine(xoff, y1, xoff, y2)
    }

    g.drawLine(xoff + xowloop, y1, xoff + xowloop, y2)

    /* special icons */

    drawRoundabout(xoff - 10, 10)
    drawArrow(xoff, xoff + xowloop, (y1 + y2) / 2 - 2)
  }

  private def setDotted(g: Graphics2D): Unit = {
    val pattern = Array(1f, 2f)
    g.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.CAP_BUTT, 5f, pattern, 0f))
  }

  private def unsetDotted(g: Graphics2D): Unit = {
    g.setStroke(new BasicStroke())
  }

  private def paintNode(): Unit = {
    val w = size / 4
    val x1 = (size / 2) - (w / 2)
    g.setColor(Color.blue)
    g.setStroke(new BasicStroke(2f))
    g.fillOval(x1, x1, w, w)
  }

  private def drawRoundabout(x: Int, y: Int): Unit = {
    val image = link.linkType match {
      case LinkType.ROUNDABOUT => Some(roundabout)
      case _ => None
    }
    image.foreach(i => g.drawImage(i, x, y, null))
  }

  private def drawArrow(xLeft: Int, xRight: Int, y: Int): Unit = {
    val down = link.linkType match {
      case LinkType.FORWARD => Some(true)
      case LinkType.BACKWARD => Some(false)
      case _ => None
    }

    if (down.isDefined) {
      if (!link.isOnewayLoopForwardPart && !link.isOnewayLoopBackwardPart) {
        drawArrow(xLeft, y, down.get)
      }
      if (link.isOnewayLoopBackwardPart && link.isOnewayLoopForwardPart) {
        drawArrow(xRight, y, !down.get)
      }
      else {
        drawArrow(xRight, y, down.get)
      }
    }
  }

  private def drawArrow(x: Int, y: Int, down: Boolean): Unit = {

    val xLeft = x - 3
    val xRight = x + 3
    val height = 7
    val yArrowPoint = if (down) y + height else y
    val yArrowStart = if (down) y else y + height

    g.drawLine(xLeft, yArrowStart, x, yArrowPoint)
    g.drawLine(xRight, yArrowStart, x, yArrowPoint)
  }
}
