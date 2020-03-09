// this class is generated, please do not modify

import {List} from "immutable";
import {Country} from "../custom/country";
import {Fact} from "../custom/fact";
import {Location} from "./location/location";
import {NodeName} from "./node-name";
import {Tags} from "../custom/tags";
import {Timestamp} from "../custom/timestamp";

export class NodeInfo {

  constructor(readonly id: number,
              readonly active: boolean,
              readonly orphan: boolean,
              readonly country: Country,
              readonly name: string,
              readonly names: List<NodeName>,
              readonly latitude: string,
              readonly longitude: string,
              readonly lastUpdated: Timestamp,
              readonly tags: Tags,
              readonly facts: List<Fact>,
              readonly location: Location,
              readonly tiles: List<string>) {
  }

  public static fromJSON(jsonObject: any): NodeInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeInfo(
      jsonObject.id,
      jsonObject.active,
      jsonObject.orphan,
      Country.fromJSON(jsonObject.country),
      jsonObject.name,
      jsonObject.names ? List(jsonObject.names.map((json: any) => NodeName.fromJSON(json))) : List(),
      jsonObject.latitude,
      jsonObject.longitude,
      Timestamp.fromJSON(jsonObject.lastUpdated),
      Tags.fromJSON(jsonObject.tags),
      jsonObject.facts ? List(jsonObject.facts.map(json => Fact.fromJSON(json))) : List(),
      Location.fromJSON(jsonObject.location),
      jsonObject.tiles ? List(jsonObject.tiles) : List()
    );
  }
}
