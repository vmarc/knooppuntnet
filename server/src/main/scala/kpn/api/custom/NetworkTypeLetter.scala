package kpn.api.custom

import kpn.api.common.NetworkType

object NetworkTypeLetter {
  
  def letter(networkType: NetworkType): String = {
    networkType match {
      case NetworkType.hiking => "w"
      case NetworkType.cycling => "c"
      case NetworkType.horseRiding => "h"
      case NetworkType.canoe => "p"
      case NetworkType.motorboat => "m"
      case NetworkType.inlineSkating => "i"
    }
  }
}
