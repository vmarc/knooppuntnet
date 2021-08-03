package kpn.core.overpass

case class QueryRelationOnly(id: Long) extends OverpassQuery {

  def name: String = s"relation-only-$id"

  def string: String = s"relation($id);out meta;"
}
