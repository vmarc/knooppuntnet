package kpn.core.tools.support

// convert -background transparent -define 'icon:auto-resize=16,24,32,64' logo.svg favicon.ico

object LogoTool {
  def main(args: Array[String]): Unit = {
    new LogoTool().create()
  }
}

class LogoTool() {

  private val width = 1000
  private val height = 1000

  private val circleRadius = 100
  private val circleCenterRadius = 400

  private val strokeWidth = 60

  private val centerX = width / 2 - 100
  private val centerY = height / 2

  private val lineRadius1 = (circleRadius / 2) + (strokeWidth / 2)
  private val lineRadius2 = circleCenterRadius - lineRadius1

  def create(): Unit = {

    val angle1 = 0d
    val angle2 = -120d
    val angle3 = -240d

    println(s"""<svg width="$width" height="$height">""")
//    println(s"""<rect width="$width" height="$height" style="stroke-width:1;stroke:rgb(0,0,0)"  fill="white"/>""")
//    println(s"""<line x1="0" y1="$centerY" x2="$width" y2="$centerY" style="stroke:rgb(255,0,0);stroke-width:1" />""")
//    println(s"""<line x1="$centerX" y1="0" x2="$centerX" y2="$height" style="stroke:rgb(255,0,0);stroke-width:1" />""")

    circle(centerX, centerY)
    outerCircle(angle1)
    outerCircle(angle2)
    outerCircle(angle3)
    println("""</svg>""")
  }

  private def outerCircle(centerDegrees: Double): Unit = {

    val radians = Math.toRadians(centerDegrees)

    val circleCenterX = Math.round(Math.cos(radians) * circleCenterRadius + centerX).toInt
    val circleCenterY = Math.round(Math.sin(radians) * circleCenterRadius + centerY).toInt

    circle(circleCenterX, circleCenterY)

    val x1 = Math.round(Math.cos(radians) * lineRadius1 + centerX)
    val y1 = Math.round(Math.sin(radians) * lineRadius1  + centerY)
    val x2 = Math.round(Math.cos(radians) * lineRadius2 + centerX)
    val y2 = Math.round(Math.sin(radians) * lineRadius2  + centerY)

    println(s"""  <line x1="$x1" y1="$y1" x2="$x2" y2="$y2" stroke="red" stroke-width="$strokeWidth"/>""")
  }

  def circle(centerX: Int, centerY: Int): Unit = {
    println(s"""  <circle cx="$centerX" cy="$centerY" r="$circleRadius" stroke="red" stroke-width="$strokeWidth" fill="white"/>""")
  }

}
