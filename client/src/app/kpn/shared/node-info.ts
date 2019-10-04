// this class is generated, please do not modify

import {List} from "immutable";
import {Country} from "./country";
import {Fact} from "./fact";
import {Location} from "./location";
import {Tags} from "./data/tags";
import {Timestamp} from "./timestamp";

export class NodeInfo {

  constructor(readonly id: number,
              readonly active: boolean,
              readonly orphan: boolean,
              readonly country: Country,
              readonly name: string,
              readonly rcnName: string,
              readonly rwnName: string,
              readonly rhnName: string,
              readonly rmnName: string,
              readonly rpnName: string,
              readonly rinName: string,
              readonly latitude: string,
              readonly longitude: string,
              readonly lastUpdated: Timestamp,
              readonly tags: Tags,
              readonly facts: List<Fact>,
              readonly location: Location) {
  }

  public static fromJSON(jsonObject): NodeInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeInfo(
      jsonObject.id,
      jsonObject.active,
      jsonObject.orphan,
      Country.fromJSON(jsonObject.country),
      jsonObject.name,
      jsonObject.rcnName,
      jsonObject.rwnName,
      jsonObject.rhnName,
      jsonObject.rmnName,
      jsonObject.rpnName,
      jsonObject.rinName,
      jsonObject.latitude,
      jsonObject.longitude,
      Timestamp.fromJSON(jsonObject.lastUpdated),
      Tags.fromJSON(jsonObject.tags),
      jsonObject.facts ? List(jsonObject.facts.map(json => Fact.fromJSON(json))) : List(),
      Location.fromJSON(jsonObject.location)
    );
  }
}
