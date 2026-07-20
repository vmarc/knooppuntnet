package kpn.api.common;

import kpn.api.custom.Tag;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record PoiAnalysis(
  ImmutableList<String> layers,
  ImmutableList<Tag> mainTags,
  ImmutableList<Tag> extraTags,
  Optional<String> name,
  Optional<String> subject,
  Optional<String> description,
  Optional<String> addressLine1,
  Optional<String> addressLine2,
  Optional<String> phone,
  Optional<String> email,
  Optional<String> fax,
  Optional<String> facebook,
  Optional<String> twitter,
  Optional<String> website,
  Optional<String> wikidata,
  Optional<String> wikipedia,
  Optional<String> molenDatabase,
  Optional<String> hollandscheMolenDatabase,
  Optional<String> image,
  Optional<String> imageLink,
  Optional<String> imageThumbnail,
  Optional<String> mapillary,
  Optional<String> wheelchair,
  Optional<String> onroerendErfgoed,
  Optional<String> openingHours,
  Optional<String> serviceTimes,
  Optional<String> cuisine,
  Optional<String> denomination
) {}

/* TODO migrate

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

*/
