// this class is generated, please do not modify

import {RouteChangeInfos} from './route-change-infos';
import {RouteInfo} from './route-info';
import {RouteReferences} from './route-references';

export class RoutePage {

  constructor(public route?: RouteInfo,
              public references?: RouteReferences,
              public routeChangeInfos?: RouteChangeInfos) {
  }

  public static fromJSON(jsonObject): RoutePage {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RoutePage();
    instance.route = RouteInfo.fromJSON(jsonObject.route);
    instance.references = RouteReferences.fromJSON(jsonObject.references);
    instance.routeChangeInfos = RouteChangeInfos.fromJSON(jsonObject.routeChangeInfos);
    return instance;
  }
}

