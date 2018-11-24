// this class is generated, please do not modify

export class ChangeType {
  readonly name: string;

  constructor(name: string) {
    this.name = name;
  }

  public static fromJSON(jsonObject): ChangeType {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeType(
      jsonObject.name
    );
  }
}
