package kpn.core.obsolete

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Image
import java.awt.geom.Line2D
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream

import javax.imageio.ImageIO
import kpn.core.analysis.Link
import kpn.core.analysis.LinkType

object OldLinkImageBuilder {

  private val size = 40

  private val arrowUp = readImage("arrowup.png")
  private val arrowDown = readImage("arrowdown.png")
  private val corners = readImage("roundedcorners.png")
  private val roundabout = readImage("roundabout.png")

  private def readImage(name: String): Image = {
    // val url = getClass().getResource("/home/marcv/git/projects/gps/images/" + name)
    // println("url=" + url)
    val file = new File("/home/marcv/git/projects/gps/images/" + name)
    ImageIO.read(file)
  }

  def build(fileName: String, link: Link): Unit = {
    val image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    val g2 = image.createGraphics()
    new OldLinkImageBuilder(g2, link).paint()
    val out = new FileOutputStream(fileName)
    ImageIO.write(image, "png", out)
    out.close()
  }
}

class OldLinkImageBuilder(g: Graphics2D, link: Link) {

  import kpn.core.obsolete.OldLinkImageBuilder._

  def paint(): Unit = {

    val width = size
    val height = size

    if (link == null || !link.isValid) {
      paintNode(g)
      return
    }

    val ymax = height - 1
    val xloop = 10
    var xowloop = 0
    if (link.isOnewayLoopForwardPart) {
      xowloop = -3
    }
    if (link.isOnewayLoopBackwardPart) {
      xowloop = 3
    }

    var xoff = width / 2
    if (link.isLoop) {
      xoff -= xloop / 2 - 1
    }
    val w = 2
    val p = 2 + w + 1
    var y1 = 0
    var y2 = 0

    if (link.hasPrev) {
      g.setColor(Color.black)
      if (link.isOnewayHead) {
        g.fillRect(xoff - 1, 0, 3, 1)
      } else {
        g.fillRect(xoff - 1 + xowloop, 0, 3, 1)
      }
      y1 = 0
    } else {
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
          g.drawRect(xoff - 1, p - 3 - w, w, w)
        } else {
          g.drawRect(xoff - 1 + xowloop, p - 1 - w, w, w)
        }
        y1 = p
      }
    }

    if (link.hasNext) {
      g.setColor(Color.black)
      if (link.isOnewayTail) {
        g.fillRect(xoff - 1, ymax, 3, 1)
      } else {
        g.fillRect(xoff - 1 + xowloop, ymax, 3, 1)
      }
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
          g.drawRect(xoff - 1, ymax - p + 3, w, w)
        } else {
          g.drawRect(xoff - 1 + xowloop, ymax - p + 1, w, w)
        }
        y2 = ymax - p
      }
    }

    /* vertical lines */
    g.setColor(Color.black)
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
    var arrow = link.linkType match {
      case LinkType.FORWARD => arrowDown
      case LinkType.BACKWARD => arrowUp
      case _ => null
    }

    if (link.linkType == LinkType.ROUNDABOUT) {
      g.drawImage(roundabout, xoff - 6, 1, null)
    }

    if (!link.isOnewayLoopForwardPart && !link.isOnewayLoopBackwardPart && (arrow != null)) {
      g.drawImage(arrow, xoff - 3, (y1 + y2) / 2 - 2, null)
    }

    if (link.isOnewayLoopBackwardPart && link.isOnewayLoopForwardPart) {
      if (arrow == arrowDown) {
        arrow = arrowUp
      } else if (arrow == arrowUp) {
        arrow = arrowDown
      }
    }

    if (arrow != null) {
      g.drawImage(arrow, xoff + xowloop - 3, (y1 + y2) / 2 - 2, null)
      ()
    }
  }

  private def setDotted(g: Graphics2D): Unit = {
    val pattern = Array(1f, 2f)
    g.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.CAP_BUTT, 5f, pattern, 0f))
  }

  private def unsetDotted(g: Graphics2D): Unit = {
    g.setStroke(new BasicStroke())
  }

  private def paintNode(g: Graphics2D): Unit = {
    // for now, just draw a cross in red and blue
    val size = 40f
    val pattern = Array(5f, 5f)
    val stroke = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.CAP_BUTT, 5f, pattern, 0f)
    val line1 = new Line2D.Float(0f, 0f, size - 1, size - 1)
    val line2 = new Line2D.Float(0f, size - 1, size - 1, 0f)
    g.setColor(Color.RED)
    g.setStroke(stroke)
    g.draw(line1)
    g.setColor(Color.BLUE)
    g.draw(line2)
  }
}
