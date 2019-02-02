// this class is generated, please do not modify

export class FactLevel {

  constructor(readonly name: string) {
  }

  public static fromJSON(jsonObject): FactLevel {
    if (!jsonObject) {
      return undefined;
    }
    return new FactLevel(
      jsonObject.name
    );
  }
}
