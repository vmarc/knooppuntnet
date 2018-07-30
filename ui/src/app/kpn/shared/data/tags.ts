// this class is generated, please do not modify

import {Tag} from './tag';

export class Tags {

  constructor(public tags?: Array<Tag>) {
  }

  public static fromJSON(jsonObject): Tags {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new Tags();
    instance.tags = jsonObject.tags ? jsonObject.tags.map(json => Tag.fromJSON(json)) : [];
    return instance;
  }
}

