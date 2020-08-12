package kpn.api.common

import kpn.api.custom.Tags

case class PoiAnalysis(
  layers: Seq[String] = Seq.empty,
  mainTags: Tags = Tags.empty,
  extraTags: Tags = Tags.empty,
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
)
