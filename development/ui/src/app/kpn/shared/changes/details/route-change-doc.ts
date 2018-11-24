// this class is generated, please do not modify

import {RouteChange} from './route-change';

export class RouteChangeDoc {
  readonly _id: string;
  readonly routeChange: RouteChange;
  readonly _rev: string;

  constructor(_id: string,
              routeChange: RouteChange,
              _rev: string) {
    this._id = _id;
    this.routeChange = routeChange;
    this._rev = _rev;
  }

  public static fromJSON(jsonObject): RouteChangeDoc {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteChangeDoc(
      jsonObject._id,
      RouteChange.fromJSON(jsonObject.routeChange),
      jsonObject._rev
    );
  }
}
