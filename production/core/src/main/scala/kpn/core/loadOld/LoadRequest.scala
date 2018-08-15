package kpn.core.loadOld

import kpn.core.overpass.OverpassQuery

case class  LoadRequest[T <: OverpassQuery](name: String, query: T) {
  def detailString: String = name + ", " + query.detailString
}
