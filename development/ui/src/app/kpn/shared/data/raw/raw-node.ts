// this class is generated, please do not modify

import {Tags} from '../tags';
import {Timestamp} from '../../timestamp';

export class RawNode {
  readonly id: number;
  readonly latitude: string;
  readonly longitude: string;
  readonly version: number;
  readonly timestamp: Timestamp;
  readonly changeSetId: number;
  readonly tags: Tags;

  constructor(id: number,
              latitude: string,
              longitude: string,
              version: number,
              timestamp: Timestamp,
              changeSetId: number,
              tags: Tags) {
    this.id = id;
    this.latitude = latitude;
    this.longitude = longitude;
    this.version = version;
    this.timestamp = timestamp;
    this.changeSetId = changeSetId;
    this.tags = tags;
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
