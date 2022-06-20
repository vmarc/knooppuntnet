package kpn.core.overpass

case class QueryRelationIds(relationIds: Seq[Long]) extends OverpassQuery {

  def name: String = "relationIds"

  def string: String = {
    val relations = relationIds.map(id => s"relation($id);").mkString
    s"($relations);out ids;"
  }

  override def detailString: String = {
    val relations = relationIds.map(_.toString).mkString("\"", "\", \"", "\"")
    s"relationIds: $relations"
  }
}
