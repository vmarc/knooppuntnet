// this class is generated, please do not modify

import {List} from "immutable";
import {Tags} from "../tags";
import {Timestamp} from "../../timestamp";

export class RawWay {

  constructor(readonly id: number,
              readonly version: number,
              readonly timestamp: Timestamp,
              readonly changeSetId: number,
              readonly nodeIds: List<number>,
              readonly tags: Tags) {
  }

  public static fromJSON(jsonObject): RawWay {
    if (!jsonObject) {
      return undefined;
    }
    return new RawWay(
      jsonObject.id,
      jsonObject.version,
      Timestamp.fromJSON(jsonObject.timestamp),
      jsonObject.changeSetId,
      jsonObject.nodeIds ? List(jsonObject.nodeIds) : List(),
      Tags.fromJSON(jsonObject.tags)
    );
  }
}
