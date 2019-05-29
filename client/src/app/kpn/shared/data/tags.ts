import {List} from "immutable";
import {Tag} from "./tag";

export class Tags {
  readonly tags: List<Tag>;

  constructor(tags: List<Tag>) {
    this.tags = tags;
  }

  has(key: string) {
    return this.tags.filter(t => t.key == key).size > 0;
  }

  get(key: string) {
    const values = this.tags.filter(t => t.key == key).map(x => x.value);
    if (values.size > 0) {
      return values.get(0);
    }
    return null;
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
