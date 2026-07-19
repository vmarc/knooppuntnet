package kpn.core.analysis

object RouteColour {

  val all: Seq[RouteColour] = Seq(
    RouteColour("aqua", 0, 255, 255),
    RouteColour("black", 0, 0, 0),
    RouteColour("blue", 0, 0, 255),
    RouteColour("brown", 165, 42, 42),
    RouteColour("gray", 128, 128, 128),
    RouteColour("green", 0, 128, 0),
    RouteColour("grey", 128, 128, 128),
    RouteColour("orange", 255, 165, 0),
    RouteColour("purple", 128, 0, 128),
    RouteColour("red", 255, 0, 0),
    RouteColour("white", 255, 255, 255),
    RouteColour("yellow", 255, 255, 0),
  )
}

case class RouteColour(name: String, r: Int, g: Int, b: Int)
