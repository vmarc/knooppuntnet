// this class is generated, please do not modify

export class Ref {
  readonly id: number;
  readonly name: string;

  constructor(id: number,
              name: string) {
    this.id = id;
    this.name = name;
  }

  public static fromJSON(jsonObject): Ref {
    if (!jsonObject) {
      return undefined;
    }
    return new Ref(
      jsonObject.id,
      jsonObject.name
    );
  }
}
