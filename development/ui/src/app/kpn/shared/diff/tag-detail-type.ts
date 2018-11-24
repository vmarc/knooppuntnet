// this class is generated, please do not modify

export class TagDetailType {
  readonly name: string;

  constructor(name: string) {
    this.name = name;
  }

  public static fromJSON(jsonObject): TagDetailType {
    if (!jsonObject) {
      return undefined;
    }
    return new TagDetailType(
      jsonObject.name
    );
  }
}
