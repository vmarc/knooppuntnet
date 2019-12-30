// this class is generated, please do not modify

import {List} from "immutable";
import {Tags} from "../custom/tags";

export class PoiPage {

  constructor(readonly elementType: string,
              readonly elementId: number,
              readonly latitude: string,
              readonly longitude: string,
              readonly layers: List<string>,
              readonly mainTags: Tags,
              readonly extraTags: Tags,
              readonly name: string,
              readonly subject: string,
              readonly description: string,
              readonly addressLine1: string,
              readonly addressLine2: string,
              readonly phone: string,
              readonly email: string,
              readonly website: string,
              readonly wikidata: string,
              readonly wikipedia: string,
              readonly image: string,
              readonly wheelchair: string) {
  }

  public static fromJSON(jsonObject): PoiPage {
    if (!jsonObject) {
      return undefined;
    }
    return new PoiPage(
      jsonObject.elementType,
      jsonObject.elementId,
      jsonObject.latitude,
      jsonObject.longitude,
      jsonObject.layers ? List(jsonObject.layers) : List(),
      Tags.fromJSON(jsonObject.mainTags),
      Tags.fromJSON(jsonObject.extraTags),
      jsonObject.name,
      jsonObject.subject,
      jsonObject.description,
      jsonObject.addressLine1,
      jsonObject.addressLine2,
      jsonObject.phone,
      jsonObject.email,
      jsonObject.website,
      jsonObject.wikidata,
      jsonObject.wikipedia,
      jsonObject.image,
      jsonObject.wheelchair
    );
  }
}
