// this class is generated, please do not modify

import {Tags} from '../data/tags';
import {Timestamp} from '../timestamp';

export class WayInfo {
  readonly id: number;
  readonly version: number;
  readonly changeSetId: number;
  readonly timestamp: Timestamp;
  readonly tags: Tags;

  constructor(id: number,
              version: number,
              changeSetId: number,
              timestamp: Timestamp,
              tags: Tags) {
    this.id = id;
    this.version = version;
    this.changeSetId = changeSetId;
    this.timestamp = timestamp;
    this.tags = tags;
  }

  public static fromJSON(jsonObject): WayInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new WayInfo(
      jsonObject.id,
      jsonObject.version,
      jsonObject.changeSetId,
      Timestamp.fromJSON(jsonObject.timestamp),
      Tags.fromJSON(jsonObject.tags)
    );
  }
}
