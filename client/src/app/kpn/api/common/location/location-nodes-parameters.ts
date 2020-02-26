// this class is generated, please do not modify

export class LocationNodesParameters {

  constructor(readonly itemsPerPage: number,
              readonly pageIndex: number) {
  }

  public static fromJSON(jsonObject): LocationNodesParameters {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationNodesParameters(
      jsonObject.itemsPerPage,
      jsonObject.pageIndex
    );
  }
}
