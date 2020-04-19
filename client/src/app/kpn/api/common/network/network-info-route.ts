// this class is generated, please do not modify

import {List} from "immutable";
import {Day} from "../common/day";
import {Fact} from "../../custom/fact";
import {Timestamp} from "../../custom/timestamp";

export class NetworkInfoRoute {

  constructor(readonly id: number,
              readonly name: string,
              readonly wayCount: number,
              readonly length: number,
              readonly role: string,
              readonly relationLastUpdated: Timestamp,
              readonly lastUpdated: Timestamp,
              readonly lastSurvey: Day,
              readonly facts: List<Fact>) {
  }

  public static fromJSON(jsonObject: any): NetworkInfoRoute {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkInfoRoute(
      jsonObject.id,
      jsonObject.name,
      jsonObject.wayCount,
      jsonObject.length,
      jsonObject.role,
      Timestamp.fromJSON(jsonObject.relationLastUpdated),
      Timestamp.fromJSON(jsonObject.lastUpdated),
      Day.fromJSON(jsonObject.lastSurvey),
      jsonObject.facts ? List(jsonObject.facts.map((json: any) => Fact.fromJSON(json))) : List()
    );
  }
}
