// this class is generated, please do not modify

export class Ref {
  constructor(readonly id: number, readonly name: string) {}

  public static fromJSON(jsonObject: any): Ref {
    if (!jsonObject) {
      return undefined;
    }
    return new Ref(jsonObject.id, jsonObject.name);
  }
}
