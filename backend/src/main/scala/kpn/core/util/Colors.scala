package kpn.core.util

object Colors {
  private val colors = Seq(
    "yellow",
    "aqua",
    "lime",
    "teal",
    "maroon",
    "fuchsia",
    "olive",
    "blue",
    "purple",
  )
}

class Colors {
  private var index = -1
  def next(): String = {
    index = index + 1
    if (index >= Colors.colors.size) {
      index = 0
    }
    Colors.colors(index)
  }
}
