import { Tag } from './tag';

export class Tags {
  constructor(readonly tags: Tag[]) {}

  static fromJSON(jsonObject: any): Tags {
    if (!jsonObject) {
      return undefined;
    }
    return new Tags(jsonObject.tags.map((json: any) => Tag.fromJSON(json)));
  }
}
