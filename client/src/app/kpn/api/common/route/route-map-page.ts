// this class is generated, please do not modify

import { RouteInfo } from './route-info';

export class RouteMapPage {
  constructor(readonly route: RouteInfo, readonly changeCount: number) {}

  public static fromJSON(jsonObject: any): RouteMapPage {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteMapPage(
      RouteInfo.fromJSON(jsonObject.route),
      jsonObject.changeCount
    );
  }
}
