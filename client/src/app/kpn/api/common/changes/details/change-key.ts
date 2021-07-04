// this class is generated, please do not modify

import { Timestamp } from '../../../custom/timestamp';

export class ChangeKey {
  constructor(
    readonly replicationNumber: number,
    readonly timestamp: Timestamp,
    readonly changeSetId: number,
    readonly elementId: number
  ) {}

  public static fromJSON(jsonObject: any): ChangeKey {
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
