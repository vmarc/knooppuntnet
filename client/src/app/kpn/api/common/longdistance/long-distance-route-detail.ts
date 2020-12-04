// this class is generated, please do not modify

export class LongDistanceRouteDetail {

  constructor(readonly id: number,
              readonly ref: string,
              readonly name: string,
              readonly description: string,
              readonly operator: string,
              readonly website: string,
              readonly wayCount: number,
              readonly osmDistance: number,
              readonly gpxDistance: number,
              readonly gpxFilename: string) {
  }

  public static fromJSON(jsonObject: any): LongDistanceRouteDetail {
    if (!jsonObject) {
      return undefined;
    }
    return new LongDistanceRouteDetail(
      jsonObject.id,
      jsonObject.ref,
      jsonObject.name,
      jsonObject.description,
      jsonObject.operator,
      jsonObject.website,
      jsonObject.wayCount,
      jsonObject.osmDistance,
      jsonObject.gpxDistance,
      jsonObject.gpxFilename
    );
  }
}
