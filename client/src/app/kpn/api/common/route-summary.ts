// this class is generated, please do not modify

import {List} from "immutable";
import {Country} from "../custom/country";
import {NetworkType} from "../custom/network-type";
import {Tags} from "../custom/tags";
import {Timestamp} from "../custom/timestamp";

export class RouteSummary {

  constructor(readonly id: number,
              readonly country: Country,
              readonly networkType: NetworkType,
              readonly name: string,
              readonly meters: number,
              readonly isBroken: boolean,
              readonly wayCount: number,
              readonly timestamp: Timestamp,
              readonly nodeNames: List<string>,
              readonly tags: Tags) {
  }

  public static fromJSON(jsonObject: any): RouteSummary {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteSummary(
      jsonObject.id,
      Country.fromJSON(jsonObject.country),
      NetworkType.fromJSON(jsonObject.networkType),
      jsonObject.name,
      jsonObject.meters,
      jsonObject.isBroken,
      jsonObject.wayCount,
      Timestamp.fromJSON(jsonObject.timestamp),
      jsonObject.nodeNames ? List(jsonObject.nodeNames) : List(),
      Tags.fromJSON(jsonObject.tags)
    );
  }
}
