package kpn.api.common

import kpn.api.custom.Tag

case class PoiAnalysis(
  layers: Seq[String] = Seq.empty,
  mainTags: Seq[Tag] = Seq.empty,
  extraTags: Seq[Tag] = Seq.empty,
  name: Option[String] = None,
  subject: Option[String] = None,
  description: Option[String] = None,
  addressLine1: Option[String] = None,
  addressLine2: Option[String] = None,
  phone: Option[String] = None,
  email: Option[String] = None,
  fax: Option[String] = None,
  facebook: Option[String] = None,
  twitter: Option[String] = None,
  website: Option[String] = None,
  wikidata: Option[String] = None,
  wikipedia: Option[String] = None,
  molenDatabase: Option[String] = None,
  hollandscheMolenDatabase: Option[String] = None,
  image: Option[String] = None,
  imageLink: Option[String] = None,
  imageThumbnail: Option[String] = None,
  mapillary: Option[String] = None,
  wheelchair: Option[String] = None,
  onroerendErfgoed: Option[String] = None,
  openingHours: Option[String] = None,
  serviceTimes: Option[String] = None,
  cuisine: Option[String] = None,
  denomination: Option[String] = None
) {
  def hasImage: Boolean = {
    image.isDefined ||
      imageLink.isDefined ||
      imageThumbnail.isDefined ||
      mapillary.isDefined
  }

  def hasLink: Boolean = {
    facebook.isDefined ||
      twitter.isDefined ||
      website.isDefined ||
      wikidata.isDefined ||
      wikipedia.isDefined ||
      molenDatabase.isDefined ||
      hollandscheMolenDatabase.isDefined ||
      onroerendErfgoed.isDefined
  }

  def address: Option[String] = {
    addressLine1 match {
      case None => addressLine2
      case Some(addressLine1Value) =>
        addressLine2 match {
          case Some(addressLine2Value) => Some(s"$addressLine1Value, $addressLine2Value")
          case None => addressLine1
        }
    }
  }

  def nameDescription: Option[String] = {
    name match {
      case Some(nameValue) => Some(nameValue)
      case None => description
    }
  }
}
