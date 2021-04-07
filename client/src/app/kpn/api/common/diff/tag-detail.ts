// this class is generated, please do not modify

import { TagDetailType } from './tag-detail-type';

export class TagDetail {
  constructor(
    readonly action: TagDetailType,
    readonly key: string,
    readonly valueBefore: string,
    readonly valueAfter: string
  ) {}

  static fromJSON(jsonObject: any): TagDetail {
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
