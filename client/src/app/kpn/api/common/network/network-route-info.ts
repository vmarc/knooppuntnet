// this class is generated, please do not modify

import {List} from "immutable";
import {Fact} from "../../custom/fact";
import {Timestamp} from "../../custom/timestamp";

export class NetworkRouteInfo {

  constructor(readonly id: number,
              readonly name: string,
              readonly wayCount: number,
              readonly length: number,
              readonly role: string,
              readonly relationLastUpdated: Timestamp,
              readonly lastUpdated: Timestamp,
              readonly facts: List<Fact>) {
  }

  public static fromJSON(jsonObject): NetworkRouteInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkRouteInfo(
      jsonObject.id,
      jsonObject.name,
      jsonObject.wayCount,
      jsonObject.length,
      jsonObject.role,
      Timestamp.fromJSON(jsonObject.relationLastUpdated),
      Timestamp.fromJSON(jsonObject.lastUpdated),
      jsonObject.facts ? List(jsonObject.facts.map(json => Fact.fromJSON(json))) : List()
    );
  }
}
