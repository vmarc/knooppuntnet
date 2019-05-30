// this class is generated, please do not modify

import {RouteInfo} from "./route-info";

export class RouteMapPage {

  constructor(readonly route: RouteInfo) {
  }

  public static fromJSON(jsonObject): RouteMapPage {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteMapPage(
      RouteInfo.fromJSON(jsonObject.route)
    );
  }
}
