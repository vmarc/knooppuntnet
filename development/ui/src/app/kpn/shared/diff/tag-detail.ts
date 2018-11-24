// this class is generated, please do not modify

import {TagDetailType} from './tag-detail-type';

export class TagDetail {
  readonly action: TagDetailType;
  readonly key: string;
  readonly valueBefore: string;
  readonly valueAfter: string;

  constructor(action: TagDetailType,
              key: string,
              valueBefore: string,
              valueAfter: string) {
    this.action = action;
    this.key = key;
    this.valueBefore = valueBefore;
    this.valueAfter = valueAfter;
  }

  public static fromJSON(jsonObject): TagDetail {
    if (!jsonObject) {
      return undefined;
    }
    return new TagDetail(
      TagDetailType.fromJSON(jsonObject.action),
      jsonObject.key,
      jsonObject.valueBefore,
      jsonObject.valueAfter
    );
  }
}
