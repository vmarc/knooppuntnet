// this class is generated, please do not modify

export class FactCountNew {

  constructor(readonly factName: string,
              readonly count: number) {
  }

  public static fromJSON(jsonObject): FactCountNew {
    if (!jsonObject) {
      return undefined;
    }
    return new FactCountNew(
      jsonObject.factName,
      jsonObject.count
    );
  }
}
