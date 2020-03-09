// this class is generated, please do not modify

import {Tags} from "../../../custom/tags";
import {Timestamp} from "../../../custom/timestamp";

export class RawNode {

  constructor(readonly id: number,
              readonly latitude: string,
              readonly longitude: string,
              readonly version: number,
              readonly timestamp: Timestamp,
              readonly changeSetId: number,
              readonly tags: Tags) {
  }

  public static fromJSON(jsonObject: any): RawNode {
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
