package kpn.core.util

import kpn.api.common.data.Tagable

object RouteSymbol {

  def from(tagable: Tagable): Option[String] = {
    tagable.tagValue("osmc:symbol").flatMap { value =>
      val trimmed = value.trim
      if (trimmed.count(_ == ':') == 1 && trimmed.endsWith(":")) {
        None
      }
      else {
        Some(value)
      }
    }
  }
}
