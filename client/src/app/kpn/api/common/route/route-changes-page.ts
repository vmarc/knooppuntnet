// this class is generated, please do not modify

import {List} from 'immutable';
import {ChangesFilter} from '../changes/filter/changes-filter';
import {RouteChangeInfo} from './route-change-info';
import {RouteInfo} from './route-info';

export class RouteChangesPage {

  constructor(readonly route: RouteInfo,
              readonly filter: ChangesFilter,
              readonly changes: List<RouteChangeInfo>,
              readonly incompleteWarning: boolean,
              readonly totalCount: number,
              readonly changeCount: number) {
  }

  public static fromJSON(jsonObject: any): RouteChangesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteChangesPage(
      RouteInfo.fromJSON(jsonObject.route),
      ChangesFilter.fromJSON(jsonObject.filter),
      jsonObject.changes ? List(jsonObject.changes.map((json: any) => RouteChangeInfo.fromJSON(json))) : List(),
      jsonObject.incompleteWarning,
      jsonObject.totalCount,
      jsonObject.changeCount
    );
  }
}
