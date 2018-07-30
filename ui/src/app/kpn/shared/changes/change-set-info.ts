// this class is generated, please do not modify

import {Tags} from '../data/tags';
import {Timestamp} from '../timestamp';

export class ChangeSetInfo {

  constructor(public id?: number,
              public createdAt?: Timestamp,
              public closedAt?: Timestamp,
              public open?: boolean,
              public commentsCount?: number,
              public tags?: Tags) {
  }

  public static fromJSON(jsonObject): ChangeSetInfo {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ChangeSetInfo();
    instance.id = jsonObject.id;
    instance.createdAt = jsonObject.createdAt;
    instance.closedAt = jsonObject.closedAt;
    instance.open = jsonObject.open;
    instance.commentsCount = jsonObject.commentsCount;
    instance.tags = jsonObject.tags;
    return instance;
  }
}

