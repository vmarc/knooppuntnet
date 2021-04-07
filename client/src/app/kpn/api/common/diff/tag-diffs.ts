// this class is generated, please do not modify

import { TagDetail } from './tag-detail';

export class TagDiffs {
  constructor(
    readonly mainTags: Array<TagDetail>,
    readonly extraTags: Array<TagDetail>
  ) {}

  static fromJSON(jsonObject: any): TagDiffs {
    if (!jsonObject) {
      return undefined;
    }
    return new TagDiffs(
      jsonObject.mainTags.map((json: any) => TagDetail.fromJSON(json)),
      jsonObject.extraTags.map((json: any) => TagDetail.fromJSON(json))
    );
  }
}
