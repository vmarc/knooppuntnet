// this class is generated, please do not modify

import {List} from "immutable";
import {Fact} from "../../custom/fact";
import {Ref} from "../common/ref";
import {Tags} from "../../custom/tags";
import {Timestamp} from "../../custom/timestamp";

export class LocationNodeInfo {

  constructor(readonly id: number,
              readonly name: string,
              readonly number: string,
              readonly latitude: string,
              readonly longitude: string,
              readonly definedInRoute: boolean,
              readonly timestamp: Timestamp,
              readonly routeReferences: List<Ref>,
              readonly facts: List<Fact>,
              readonly tags: Tags) {
  }

  public static fromJSON(jsonObject): LocationNodeInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationNodeInfo(
      jsonObject.id,
      jsonObject.name,
      jsonObject.number,
      jsonObject.latitude,
      jsonObject.longitude,
      jsonObject.definedInRoute,
      Timestamp.fromJSON(jsonObject.timestamp),
      jsonObject.routeReferences ? List(jsonObject.routeReferences.map(json => Ref.fromJSON(json))) : List(),
      jsonObject.facts ? List(jsonObject.facts.map(json => Fact.fromJSON(json))) : List(),
      Tags.fromJSON(jsonObject.tags)
    );
  }
}
