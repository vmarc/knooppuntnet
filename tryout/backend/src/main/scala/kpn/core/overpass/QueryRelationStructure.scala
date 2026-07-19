package kpn.core.overpass

case class QueryRelationStructure(id: Long) extends OverpassQuery {

  def name: String = s"relation-structure-$id"

  def string: String = s"relation($id);(._;rel(r););(._;rel(r););(._;rel(r););(._;rel(r););(._;rel(r););out meta;"
}
