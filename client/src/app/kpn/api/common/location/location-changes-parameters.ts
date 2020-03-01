// this class is generated, please do not modify

export class LocationChangesParameters {

  constructor(readonly itemsPerPage: number,
              readonly pageIndex: number) {
  }

  public static fromJSON(jsonObject): LocationChangesParameters {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationChangesParameters(
      jsonObject.itemsPerPage,
      jsonObject.pageIndex
    );
  }
}
