export class Fact {

  constructor(readonly name: string) {
  }

  public static fromJSON(jsonObject): Fact {
    if (!jsonObject) {
      return undefined;
    }
    return new Fact(jsonObject);
  }
}
