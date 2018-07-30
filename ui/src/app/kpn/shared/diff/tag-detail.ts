// this class is generated, please do not modify

import {TagDetailType} from './tag-detail-type';

export class TagDetail {

  constructor(public action?: TagDetailType,
              public key?: string,
              public valueBefore?: string,
              public valueAfter?: string) {
  }

  public static fromJSON(jsonObject): TagDetail {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new TagDetail();
    instance.action = jsonObject.action;
    instance.key = jsonObject.key;
    instance.valueBefore = jsonObject.valueBefore;
    instance.valueAfter = jsonObject.valueAfter;
    return instance;
  }
}

