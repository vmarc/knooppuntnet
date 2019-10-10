package kpn.core.overpass

case class QueryNodes(name: String, nodeIds: Seq[Long]) extends OverpassQuery {

  def string: String = {
    val nodes = nodeIds.map(id => s"node($id);").mkString
    s"($nodes);out meta;"
  }

  override def detailString: String = {
    val nodes = nodeIds.map(_.toString).mkString("\"", "\", \"", "\"")
    s"$name, nodeIds: $nodes"
  }
}
