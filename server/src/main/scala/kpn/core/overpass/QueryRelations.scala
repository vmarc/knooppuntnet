package kpn.core.overpass

case class QueryRelations(relationIds: Seq[Long]) extends OverpassQuery {

  def name: String = "relations"

  def string: String = {
    val relations = relationIds.map(id => s"relation($id);(>>;);").mkString
    s"($relations);out meta;"
  }

  override def detailString: String = {
    val relations = relationIds.map(_.toString).mkString("\"", "\", \"", "\"")
    s"relationIds: $relations"
  }
}
