// this class is generated, please do not modify

export class FactLevel {
  readonly name: string;

  constructor(name: string) {
    this.name = name;
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
