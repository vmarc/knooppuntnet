package kpn.platform

import kpn.shared.Timestamp

class PlatformUtilImpl extends PlatformUtil {
  override def toLocal(timestamp: Timestamp): Timestamp = {
    throw new RuntimeException("should not call PlatformUtilImpl.toLocal from client code")
  }
}

