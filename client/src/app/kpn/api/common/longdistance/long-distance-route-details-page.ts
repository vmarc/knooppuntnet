// this class is generated, please do not modify

export class LongDistanceRouteDetailsPage {

  constructor(readonly id: number,
              readonly ref: string,
              readonly name: string,
              readonly nameNl: string,
              readonly nameEn: string,
              readonly nameDe: string,
              readonly nameFr: string,
              readonly description: string,
              readonly operator: string,
              readonly website: string,
              readonly wayCount: number,
              readonly osmDistance: number,
              readonly gpxDistance: number,
              readonly gpxFilename: string) {
  }

  public static fromJSON(jsonObject: any): LongDistanceRouteDetailsPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LongDistanceRouteDetailsPage(
      jsonObject.id,
      jsonObject.ref,
      jsonObject.name,
      jsonObject.nameNl,
      jsonObject.nameEn,
      jsonObject.nameDe,
      jsonObject.nameFr,
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
