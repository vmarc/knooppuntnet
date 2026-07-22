package kpn.core.overpass

case class QueryCenters(elementType: String, elementIds: Seq[Long]) extends OverpassQuery {

  def name: String = s"$elementType-centers"

  def string: String = {
    val elements = elementIds.map(id => s"$elementType($id);").mkString
    s"($elements);out center;"
  }

  override def detailString: String = {
    val nodes = elementIds.map(_.toString).mkString("\"", "\", \"", "\"")
    s"$name, elementIds: $nodes"
  }
}
