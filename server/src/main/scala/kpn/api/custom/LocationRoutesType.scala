package kpn.api.custom

object LocationRoutesType {

  def withName(name: String): Option[LocationRoutesType] = {
    allValues.find(_.name == name)
  }

  val all: LocationRoutesType = LocationRoutesType("all")
  val facts: LocationRoutesType = LocationRoutesType("facts")
  val inaccessible: LocationRoutesType = LocationRoutesType("inaccessible")
  val survey: LocationRoutesType = LocationRoutesType("survey")

  val allValues: Seq[LocationRoutesType] = Seq(all, facts, inaccessible, survey)
}

case class LocationRoutesType(name: String)
