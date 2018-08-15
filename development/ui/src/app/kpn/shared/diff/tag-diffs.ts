// this class is generated, please do not modify

import {TagDetail} from './tag-detail';

export class TagDiffs {

  constructor(public mainTags?: Array<TagDetail>,
              public extraTags?: Array<TagDetail>) {
  }

  public static fromJSON(jsonObject): TagDiffs {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new TagDiffs();
    instance.mainTags = jsonObject.mainTags ? jsonObject.mainTags.map(json => TagDetail.fromJSON(json)) : [];
    instance.extraTags = jsonObject.extraTags ? jsonObject.extraTags.map(json => TagDetail.fromJSON(json)) : [];
    return instance;
  }
}

