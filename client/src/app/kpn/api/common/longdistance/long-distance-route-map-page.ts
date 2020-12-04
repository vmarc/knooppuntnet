// this class is generated, please do not modify

import {Bounds} from '../bounds';

export class LongDistanceRouteMapPage {

  constructor(readonly id: number,
              readonly ref: string,
              readonly name: string,
              readonly nameNl: string,
              readonly nameEn: string,
              readonly nameDe: string,
              readonly nameFr: string,
              readonly bounds: Bounds,
              readonly gpxFilename: string,
              readonly osmGeometry: string,
              readonly gpxGeometry: string,
              readonly okGeometry: string,
              readonly nokGeometry: string) {
  }

  public static fromJSON(jsonObject: any): LongDistanceRouteMapPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LongDistanceRouteMapPage(
      jsonObject.id,
      jsonObject.ref,
      jsonObject.name,
      jsonObject.nameNl,
      jsonObject.nameEn,
      jsonObject.nameDe,
      jsonObject.nameFr,
      Bounds.fromJSON(jsonObject.bounds),
      jsonObject.gpxFilename,
      jsonObject.osmGeometry,
      jsonObject.gpxGeometry,
      jsonObject.okGeometry,
      jsonObject.nokGeometry
    );
  }
}
