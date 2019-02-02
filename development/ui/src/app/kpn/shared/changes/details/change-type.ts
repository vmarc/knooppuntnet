// this class is generated, please do not modify

export class ChangeType {

  constructor(readonly name: string) {
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
