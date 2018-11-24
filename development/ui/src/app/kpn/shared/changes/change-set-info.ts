// this class is generated, please do not modify

import {Tags} from '../data/tags';
import {Timestamp} from '../timestamp';

export class ChangeSetInfo {
  readonly id: number;
  readonly createdAt: Timestamp;
  readonly closedAt: Timestamp;
  readonly open: boolean;
  readonly commentsCount: number;
  readonly tags: Tags;

  constructor(id: number,
              createdAt: Timestamp,
              closedAt: Timestamp,
              open: boolean,
              commentsCount: number,
              tags: Tags) {
    this.id = id;
    this.createdAt = createdAt;
    this.closedAt = closedAt;
    this.open = open;
    this.commentsCount = commentsCount;
    this.tags = tags;
  }

  public static fromJSON(jsonObject): ChangeSetInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetInfo(
      jsonObject.id,
      Timestamp.fromJSON(jsonObject.createdAt),
      Timestamp.fromJSON(jsonObject.closedAt),
      jsonObject.open,
      jsonObject.commentsCount,
      Tags.fromJSON(jsonObject.tags)
    );
  }
}
