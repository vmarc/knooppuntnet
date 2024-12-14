package kpn.api.custom

import kpn.api.common.NetworkScope

object NetworkScopeLetter {
  def letter(scope: NetworkScope): String = {
    scope match {
      case NetworkScope.local => "l"
      case NetworkScope.regional => "r"
      case NetworkScope.national => "n"
      case NetworkScope.international => "i"
    }
  }
}
