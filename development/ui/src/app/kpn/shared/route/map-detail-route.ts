// this class is generated, please do not modify

import {RouteInfo} from './route-info';
import {RouteReferences} from './route-references';

export class MapDetailRoute {
  readonly route: RouteInfo;
  readonly references: RouteReferences;

  constructor(route: RouteInfo,
              references: RouteReferences) {
    this.route = route;
    this.references = references;
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
