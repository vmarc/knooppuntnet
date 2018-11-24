// this class is generated, please do not modify

import {Timestamp} from '../../timestamp';

export class ChangeKey {
  readonly replicationNumber: number;
  readonly timestamp: Timestamp;
  readonly changeSetId: number;
  readonly elementId: number;

  constructor(replicationNumber: number,
              timestamp: Timestamp,
              changeSetId: number,
              elementId: number) {
    this.replicationNumber = replicationNumber;
    this.timestamp = timestamp;
    this.changeSetId = changeSetId;
    this.elementId = elementId;
  }

  public static fromJSON(jsonObject): ChangeKey {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeKey(
      jsonObject.replicationNumber,
      Timestamp.fromJSON(jsonObject.timestamp),
      jsonObject.changeSetId,
      jsonObject.elementId
    );
  }
}
