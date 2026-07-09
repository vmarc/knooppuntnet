package kpn.core.overpass

case class QueryRelationTopLevel(id: Long) extends OverpassQuery {

  def name: String = s"relation-top-level-$id"

  def string: String = s"relation($id);(._;>;);out meta;"
}
