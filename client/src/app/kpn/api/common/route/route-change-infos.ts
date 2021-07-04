// this class is generated, please do not modify

import { List } from 'immutable';
import { RouteChangeInfo } from './route-change-info';

export class RouteChangeInfos {
  constructor(
    readonly changes: List<RouteChangeInfo>,
    readonly incompleteWarning: boolean
  ) {}

  public static fromJSON(jsonObject: any): RouteChangeInfos {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteChangeInfos(
      jsonObject.changes
        ? List(
            jsonObject.changes.map((json: any) =>
              RouteChangeInfo.fromJSON(json)
            )
          )
        : List(),
      jsonObject.incompleteWarning
    );
  }
}
