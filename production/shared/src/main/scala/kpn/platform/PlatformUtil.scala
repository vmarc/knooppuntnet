package kpn.platform

import kpn.shared.Timestamp

trait PlatformUtil {
  def toLocal(timestamp: Timestamp): Timestamp
}

