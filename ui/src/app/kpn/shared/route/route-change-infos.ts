// this class is generated, please do not modify

import {RouteChangeInfo} from './route-change-info';

export class RouteChangeInfos {

  constructor(public changes?: Array<RouteChangeInfo>,
              public incompleteWarning?: boolean) {
  }

  public static fromJSON(jsonObject): RouteChangeInfos {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RouteChangeInfos();
    instance.changes = jsonObject.changes ? jsonObject.changes.map(json => RouteChangeInfo.fromJSON(json)) : [];
    instance.incompleteWarning = jsonObject.incompleteWarning;
    return instance;
  }
}

