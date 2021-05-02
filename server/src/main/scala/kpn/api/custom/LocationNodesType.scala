package kpn.api.custom

object LocationNodesType {

  def withName(name: String): Option[LocationNodesType] = {
    allValues.find(_.name == name)
  }

  val all: LocationNodesType = LocationNodesType("all")
  val facts: LocationNodesType = LocationNodesType("facts")
  val survey: LocationNodesType = LocationNodesType("survey")

  val allValues: Seq[LocationNodesType] = Seq(all, facts, survey)
}

case class LocationNodesType(name: String)
