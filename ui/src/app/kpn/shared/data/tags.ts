import {Tag} from './tag';

export class Tags {

  constructor(public tags?: Array<Tag>) {
  }

  public static fromJSON(jsonObject): Tags {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new Tags();
    instance.tags = jsonObject.map(json => Tag.fromJSON(json));
    return instance;
  }
}

