// this class is generated, please do not modify

export class LongDistanceRouteChangesPage {

  constructor(readonly id: number,
              readonly ref: string,
              readonly name: string) {
  }

  public static fromJSON(jsonObject: any): LongDistanceRouteChangesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LongDistanceRouteChangesPage(
      jsonObject.id,
      jsonObject.ref,
      jsonObject.name
    );
  }
}
