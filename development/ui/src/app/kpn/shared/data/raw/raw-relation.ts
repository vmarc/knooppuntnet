// this class is generated, please do not modify

import {List} from 'immutable';
import {RawMember} from './raw-member';
import {Tags} from '../tags';
import {Timestamp} from '../../timestamp';

export class RawRelation {
  readonly id: number;
  readonly version: number;
  readonly timestamp: Timestamp;
  readonly changeSetId: number;
  readonly members: List<RawMember>;
  readonly tags: Tags;

  constructor(id: number,
              version: number,
              timestamp: Timestamp,
              changeSetId: number,
              members: List<RawMember>,
              tags: Tags) {
    this.id = id;
    this.version = version;
    this.timestamp = timestamp;
    this.changeSetId = changeSetId;
    this.members = members;
    this.tags = tags;
  }

  public static fromJSON(jsonObject): RawRelation {
    if (!jsonObject) {
      return undefined;
    }
    return new RawRelation(
      jsonObject.id,
      jsonObject.version,
      Timestamp.fromJSON(jsonObject.timestamp),
      jsonObject.changeSetId,
      jsonObject.members ? List(jsonObject.members.map(json => RawMember.fromJSON(json))) : List(),
      Tags.fromJSON(jsonObject.tags)
    );
  }
}
