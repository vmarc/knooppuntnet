package kpn.api.custom

object Country {

  def withDomain(domain: String): Option[Country] = {
    all.find(_.domain == domain)
  }

  val nl: Country = Country("nl")
  val be: Country = Country("be")
  val de: Country = Country("de")
  val fr: Country = Country("fr")
  val all: Seq[Country] = Seq(nl, be, de, fr)
}

case class Country(domain: String)
