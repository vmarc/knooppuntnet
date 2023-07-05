package kpn.core.util

import kpn.api.custom.Tags

object RouteSymbol {

  private val empty = ".[^:]]:$".r

  def from(tags: Tags): Option[String] = {
    tags("osmc:symbol").flatMap { value =>
      val trimmed = value.trim
      if (trimmed.filter(_ == ':').size == 1 && trimmed.endsWith(":")) {
        None
      }
      else {
        Some(value)
      }
    }
  }
}
