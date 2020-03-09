// this class is generated, please do not modify

export class LocationRoutesParameters {

  constructor(readonly itemsPerPage: number,
              readonly pageIndex: number) {
  }

  public static fromJSON(jsonObject: any): LocationRoutesParameters {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationRoutesParameters(
      jsonObject.itemsPerPage,
      jsonObject.pageIndex
    );
  }
}
