// this class is generated, please do not modify

import {RouteChange} from './route-change';

export class RouteChangeDoc {

  constructor(public _id?: string,
              public routeChange?: RouteChange,
              public _rev?: string) {
  }

  public static fromJSON(jsonObject): RouteChangeDoc {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RouteChangeDoc();
    instance._id = jsonObject._id;
    instance.routeChange = jsonObject.routeChange;
    instance._rev = jsonObject._rev;
    return instance;
  }
}

