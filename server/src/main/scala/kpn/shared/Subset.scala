package kpn.shared

object Subset {

  val beHiking = Subset(Country.be, NetworkType.hiking)
  val beBicycle = Subset(Country.be, NetworkType.bicycle)
  val beHorseRiding = Subset(Country.be, NetworkType.horseRiding)

  val nlHiking = Subset(Country.nl, NetworkType.hiking)
  val nlBicycle = Subset(Country.nl, NetworkType.bicycle)
  val nlHorseRiding = Subset(Country.nl, NetworkType.horseRiding)
  val nlCanoe = Subset(Country.nl, NetworkType.canoe)
  val nlMotorboat = Subset(Country.nl, NetworkType.motorboat)
  val nlInlineSkates = Subset(Country.nl, NetworkType.inlineSkates)

  val deHiking = Subset(Country.de, NetworkType.hiking)
  val deBicycle = Subset(Country.de, NetworkType.bicycle)
  val deHorseRiding = Subset(Country.de, NetworkType.horseRiding)

  val frHiking = Subset(Country.fr, NetworkType.hiking)
  val frBicycle = Subset(Country.fr, NetworkType.bicycle)
  val frHorseRiding = Subset(Country.fr, NetworkType.horseRiding)

  val all: Seq[Subset] = Seq(
    nlBicycle,
    beBicycle,
    deBicycle,
    frBicycle,
    nlHiking,
    beHiking,
    deHiking,
    frHiking,
    nlHorseRiding,
    beHorseRiding,
    deHorseRiding,
    frHorseRiding,
    nlCanoe,
    nlMotorboat,
    nlInlineSkates
  )

  def ofNewName(domain: String, networkTypeNewName: String): Option[Subset] = {
    all.find(s => s.country.domain == domain && s.networkType.name == networkTypeNewName)
  }

  def of(country: Country, networkType: NetworkType): Option[Subset] = {
    all.find(s => s.country == country && s.networkType == networkType)
  }
}

case class Subset(country: Country, networkType: NetworkType) extends Ordered[Subset] {

  def key: String = country.domain + ":" + networkType.name

  def name: String = country.domain + "-" + networkType.name

  def string: String = country.domain + "/" + networkType.name

  import scala.math.Ordered.orderingToOrdered

  def compare(that: Subset): Int = (this.country.domain, this.networkType.name).compare((that.country.domain, that.networkType.name))
}
