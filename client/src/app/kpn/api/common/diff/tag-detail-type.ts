// this class is generated, please do not modify

export class TagDetailType {
  constructor(readonly name: string) {}

  static fromJSON(jsonObject: any): TagDetailType {
    if (!jsonObject) {
      return undefined;
    }
    return new TagDetailType(jsonObject.name);
  }
}
