package kpn.core.overpass

case class QueryRelations(name: String, relationIds: Seq[Long]) extends OverpassQuery {

  def string: String = {
    val relations = relationIds.map(id => s"relation($id);(>>;);").mkString
    s"($relations);out meta;"
  }

  override def detailString: String = {
    val relations = relationIds.map(_.toString).mkString("\"", "\", \"", "\"")
    s"$name, relationIds: $relations"
  }
}
