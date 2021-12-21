package kpn.api.common.location

object Location {
  def empty: Location = {
    Location(Seq.empty)
  }
}

case class Location(names: Seq[String])
