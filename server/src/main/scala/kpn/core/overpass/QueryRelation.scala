package kpn.core.overpass

case class QueryRelation(id: Long) extends OverpassQuery {

  def name: String = s"relation-$id"

  def string: String = s"relation($id);(>>;);out meta;"
}
