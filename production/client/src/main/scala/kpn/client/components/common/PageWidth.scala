// TODO migrate to Angular
package kpn.client.components.common

import org.scalajs.dom

object PageWidth extends Enumeration {
  val Small, Medium, Large, VeryLarge = Value

  val SmallMaxWidth = 768
  val MediumMaxWidth = 1024
  val LargeMaxWidth = 1300

  def isSmall: Boolean = current == Small
  def isMedium: Boolean = current == Medium
  def isLarge: Boolean = current == Large
  def isVeryLarge: Boolean = current == VeryLarge


  def current: PageWidth.Value = {
    val width = dom.window.innerWidth
    if (width <= SmallMaxWidth) {
      Small
    }
    else if (width <= MediumMaxWidth) {
      Medium
    }
    else if (width <= LargeMaxWidth) {
      Large
    }
    else {
      VeryLarge
    }
  }
}
