// this class is generated, please do not modify

import {RouteChangeInfo} from './route-change-info';

export class RouteChangeInfos {

  constructor(readonly changes: Array<RouteChangeInfo>,
              readonly incompleteWarning: boolean) {
  }

  public static fromJSON(jsonObject: any): RouteChangeInfos {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteChangeInfos(
      jsonObject.changes.map((json: any) => RouteChangeInfo.fromJSON(json)),
      jsonObject.incompleteWarning
    );
  }
}
