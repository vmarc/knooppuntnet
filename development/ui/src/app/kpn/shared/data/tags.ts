import {List} from 'immutable';
import {Tag} from './tag';

export class Tags {
  readonly tags: List<Tag>;

  constructor(tags: List<Tag>) {
    this.tags = tags;
  }

  public static fromJSON(jsonObject): Tags {
    if (!jsonObject) {
      return undefined;
    }
    return new Tags(
      List(jsonObject.map(json => Tag.fromJSON(json)))
    );
  }
}
