// this class is generated, please do not modify

import {Timestamp} from '../timestamp';

export class MetaData {
  readonly version: number;
  readonly timestamp: Timestamp;
  readonly changeSetId: number;

  constructor(version: number,
              timestamp: Timestamp,
              changeSetId: number) {
    this.version = version;
    this.timestamp = timestamp;
    this.changeSetId = changeSetId;
  }

  public static fromJSON(jsonObject): MetaData {
    if (!jsonObject) {
      return undefined;
    }
    return new MetaData(
      jsonObject.version,
      Timestamp.fromJSON(jsonObject.timestamp),
      jsonObject.changeSetId
    );
  }
}
