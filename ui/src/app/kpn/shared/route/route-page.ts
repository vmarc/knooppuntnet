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
    instance.route = jsonObject.route;
    instance.references = jsonObject.references;
    instance.routeChangeInfos = jsonObject.routeChangeInfos;
    return instance;
  }
}

