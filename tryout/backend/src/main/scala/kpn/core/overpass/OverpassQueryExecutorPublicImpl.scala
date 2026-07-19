package kpn.core.overpass

class OverpassQueryExecutorPublicImpl(url: String) extends OverpassQueryExecutorRemoteImpl(url) {
  override def timeout: Option[Long] = None

  override def maxSize: Option[Long] = None
}
