// this class is generated, please do not modify

import {List} from 'immutable';
import {Bounds} from '../bounds';
import {LongDistanceRouteNokSegment} from './long-distance-route-nok-segment';
import {LongDistanceRouteSegment} from './long-distance-route-segment';

export class LongDistanceRoute {

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
              readonly bounds: Bounds,
              readonly gpxFilename: string,
              readonly osmSegments: List<LongDistanceRouteSegment>,
              readonly gpxGeometry: string,
              readonly okGeometry: string,
              readonly nokSegments: List<LongDistanceRouteNokSegment>) {
  }

  public static fromJSON(jsonObject: any): LongDistanceRoute {
    if (!jsonObject) {
      return undefined;
    }
    return new LongDistanceRoute(
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
      Bounds.fromJSON(jsonObject.bounds),
      jsonObject.gpxFilename,
      jsonObject.osmSegments ? List(jsonObject.osmSegments.map((json: any) => LongDistanceRouteSegment.fromJSON(json))) : List(),
      jsonObject.gpxGeometry,
      jsonObject.okGeometry,
      jsonObject.nokSegments ? List(jsonObject.nokSegments.map((json: any) => LongDistanceRouteNokSegment.fromJSON(json))) : List()
    );
  }
}
