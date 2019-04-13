// this class is generated, please do not modify

import {Tags} from "../tags";
import {Timestamp} from "../../timestamp";

export class RawNode {

  constructor(readonly id: number,
              readonly latitude: string,
              readonly longitude: string,
              readonly version: number,
              readonly timestamp: Timestamp,
              readonly changeSetId: number,
              readonly tags: Tags) {
  }

  public static fromJSON(jsonObject): RawNode {
    if (!jsonObject) {
      return undefined;
    }
    return new RawNode(
      jsonObject.id,
      jsonObject.latitude,
      jsonObject.longitude,
      jsonObject.version,
      Timestamp.fromJSON(jsonObject.timestamp),
      jsonObject.changeSetId,
      Tags.fromJSON(jsonObject.tags)
    );
  }
}
