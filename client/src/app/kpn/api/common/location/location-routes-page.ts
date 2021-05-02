// this class is generated, please do not modify

import { TimeInfo } from '../time-info';
import { LocationRouteInfo } from './location-route-info';
import { LocationSummary } from './location-summary';

export class LocationRoutesPage {
  constructor(
    readonly timeInfo: TimeInfo,
    readonly summary: LocationSummary,
    readonly routeCount: number,
    readonly allRouteCount: number,
    readonly factsRouteCount: number,
    readonly inaccessibleRouteCount: number,
    readonly surveyRouteCount: number,
    readonly routes: Array<LocationRouteInfo>
  ) {}

  static fromJSON(jsonObject: any): LocationRoutesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationRoutesPage(
      TimeInfo.fromJSON(jsonObject.timeInfo),
      LocationSummary.fromJSON(jsonObject.summary),
      jsonObject.routeCount,
      jsonObject.allRouteCount,
      jsonObject.factsRouteCount,
      jsonObject.inaccessibleRouteCount,
      jsonObject.surveyRouteCount,
      jsonObject.routes?.map((json: any) => LocationRouteInfo.fromJSON(json))
    );
  }
}
