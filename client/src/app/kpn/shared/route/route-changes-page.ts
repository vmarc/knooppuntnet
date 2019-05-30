// this class is generated, please do not modify

import {List} from "immutable";
import {RouteChangeInfo} from "./route-change-info";
import {RouteInfo} from "./route-info";

export class RouteChangesPage {

  constructor(readonly route: RouteInfo,
              readonly changes: List<RouteChangeInfo>,
              readonly incompleteWarning: boolean,
              readonly totalCount: number) {
  }

  public static fromJSON(jsonObject): RouteChangesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteChangesPage(
      RouteInfo.fromJSON(jsonObject.route),
      jsonObject.changes ? List(jsonObject.changes.map(json => RouteChangeInfo.fromJSON(json))) : List(),
      jsonObject.incompleteWarning,
      jsonObject.totalCount
    );
  }
}
