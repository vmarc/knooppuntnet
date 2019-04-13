// this class is generated, please do not modify

import {RouteInfo} from "./route-info";
import {RouteReferences} from "./route-references";

export class MapDetailRoute {

  constructor(readonly route: RouteInfo,
              readonly references: RouteReferences) {
  }

  public static fromJSON(jsonObject): MapDetailRoute {
    if (!jsonObject) {
      return undefined;
    }
    return new MapDetailRoute(
      RouteInfo.fromJSON(jsonObject.route),
      RouteReferences.fromJSON(jsonObject.references)
    );
  }
}
