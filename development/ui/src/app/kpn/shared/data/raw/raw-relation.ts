// this class is generated, please do not modify

import {List} from "immutable";
import {RawMember} from "./raw-member";
import {Tags} from "../tags";
import {Timestamp} from "../../timestamp";

export class RawRelation {

  constructor(readonly id: number,
              readonly version: number,
              readonly timestamp: Timestamp,
              readonly changeSetId: number,
              readonly members: List<RawMember>,
              readonly tags: Tags) {
  }

  public static fromJSON(jsonObject): RawRelation {
    if (!jsonObject) {
      return undefined;
    }
    return new RawRelation(
      jsonObject.id,
      jsonObject.version,
      Timestamp.fromJSON(jsonObject.timestamp),
      jsonObject.changeSetId,
      jsonObject.members ? List(jsonObject.members.map(json => RawMember.fromJSON(json))) : List(),
      Tags.fromJSON(jsonObject.tags)
    );
  }
}
