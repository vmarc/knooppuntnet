import {Address, Poi, PoiInformation} from "../../../../model";

export function convertPoiToPoiInformation(poi: Poi): PoiInformation {

  const poiInformation = new PoiInformation();
  const address = new Address();
  const name = poi.tags.filter(p => p.key === "name").pop();
  const description = poi.tags.filter(p => p.key === "description").pop();
  const street = poi.tags.filter(p => p.key === "addr:street").pop();
  const house = poi.tags.filter(p => p.key === "addr:housenumber").pop();
  const postcode = poi.tags.filter(p => p.key === "addr:postcode").pop();
  const city = poi.tags.filter(p => p.key === "addr:city").pop();
  const country = poi.tags.filter(p => p.key === "addr:country").pop();
  const website = poi.tags.filter(p => p.key === "website").pop();
  const image = poi.tags.filter(p => p.key === "image").pop();

  poiInformation.name = name !== undefined ? name.value : "";
  poiInformation.description = description !== undefined ? description.value : "";
  poiInformation.image = image !== undefined ? image.value : "";
  address.street = street !== undefined ? street.value : "";
  address.housenumber = house !== undefined ? house.value : "";
  address.postcode = postcode !== undefined ? postcode.value : "";
  address.city = city !== undefined ? city.value : "";
  address.country = country !== undefined ? country.value : "";

  if (website !== undefined) {
    if (website.value.indexOf("http") < 0) {
      poiInformation.website = "http://" + website.value;
    } else {
      poiInformation.website = website.value;
    }
  }

  poiInformation.address = address;

  return poiInformation;
}
