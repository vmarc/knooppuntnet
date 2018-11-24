// this class is generated, please do not modify

import {List} from 'immutable';
import {RouteChangeInfo} from './route-change-info';

export class RouteChangeInfos {
  readonly changes: List<RouteChangeInfo>;
  readonly incompleteWarning: boolean;

  constructor(changes: List<RouteChangeInfo>,
              incompleteWarning: boolean) {
    this.changes = changes;
    this.incompleteWarning = incompleteWarning;
  }

  public static fromJSON(jsonObject): RouteChangeInfos {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteChangeInfos(
      jsonObject.changes ? List(jsonObject.changes.map(json => RouteChangeInfo.fromJSON(json))) : List(),
      jsonObject.incompleteWarning
    );
  }
}
