export class Tag {
  readonly key: string;
  readonly value: string;

  constructor(key: string,
              value: string) {
    this.key = key;
    this.value = value;
  }

  public static fromJSON(jsonObject): Tag {
    if (!jsonObject) {
      return undefined;
    }
    return new Tag(
      jsonObject[0],
      jsonObject[1]
    );
  }
}
