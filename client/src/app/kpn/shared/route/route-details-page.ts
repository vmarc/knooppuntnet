// this class is generated, please do not modify

import {RouteInfo} from "./route-info";
import {RouteReferences} from "./route-references";

export class RouteDetailsPage {

  constructor(readonly route: RouteInfo,
              readonly references: RouteReferences,
              readonly changeCount: number) {
  }

  public static fromJSON(jsonObject): RouteDetailsPage {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteDetailsPage(
      RouteInfo.fromJSON(jsonObject.route),
      RouteReferences.fromJSON(jsonObject.references),
      jsonObject.changeCount
    );
  }
}
