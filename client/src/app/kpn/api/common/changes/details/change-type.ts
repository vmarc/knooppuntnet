// this class is generated, please do not modify

export class ChangeType {
  constructor(readonly name: string) {}

  public static fromJSON(jsonObject: any): ChangeType {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeType(jsonObject.name);
  }
}
