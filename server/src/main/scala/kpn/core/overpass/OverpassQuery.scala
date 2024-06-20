package kpn.core.overpass

object OverpassQuery {
  def from(queryString: String): OverpassQuery = {
    new OverpassQuery() {
      def string: String = queryString

      def name: String = ""
    }
  }
}

trait OverpassQuery {
  def string: String

  def name: String

  def detailString: String = name
}
