// this class is generated, please do not modify

import { Tags } from '../../../custom/tags';
import { Timestamp } from '../../../custom/timestamp';
import { RawMember } from './raw-member';

export class RawRelation {
  constructor(
    readonly id: number,
    readonly version: number,
    readonly timestamp: Timestamp,
    readonly changeSetId: number,
    readonly members: Array<RawMember>,
    readonly tags: Tags
  ) {}

  public static fromJSON(jsonObject: any): RawRelation {
    if (!jsonObject) {
      return undefined;
    }
    return new RawRelation(
      jsonObject.id,
      jsonObject.version,
      Timestamp.fromJSON(jsonObject.timestamp),
      jsonObject.changeSetId,
      jsonObject.members.map((json: any) => RawMember.fromJSON(json)),
      Tags.fromJSON(jsonObject.tags)
    );
  }
}
