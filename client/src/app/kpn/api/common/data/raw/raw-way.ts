// this class is generated, please do not modify

import { Tags } from '../../../custom/tags';
import { Timestamp } from '../../../custom/timestamp';

export class RawWay {
  constructor(
    readonly id: number,
    readonly version: number,
    readonly timestamp: Timestamp,
    readonly changeSetId: number,
    readonly nodeIds: Array<number>,
    readonly tags: Tags
  ) {}

  public static fromJSON(jsonObject: any): RawWay {
    if (!jsonObject) {
      return undefined;
    }
    return new RawWay(
      jsonObject.id,
      jsonObject.version,
      Timestamp.fromJSON(jsonObject.timestamp),
      jsonObject.changeSetId,
      jsonObject.nodeIds,
      Tags.fromJSON(jsonObject.tags)
    );
  }
}
