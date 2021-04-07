// this class is generated, please do not modify

export class Ids {
  constructor(readonly ids: Array<number>) {}

  static fromJSON(jsonObject: any): Ids {
    if (!jsonObject) {
      return undefined;
    }
    return new Ids(jsonObject.ids);
  }
}
