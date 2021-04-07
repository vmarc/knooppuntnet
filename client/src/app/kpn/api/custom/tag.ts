export class Tag {
  constructor(readonly key: string, readonly value: string) {}

  static fromJSON(jsonObject: any): Tag {
    if (!jsonObject) {
      return undefined;
    }
    return new Tag(jsonObject.key, jsonObject.value);
  }
}
