// this class is generated, please do not modify

import {RouteChange} from './route-change';

export class RouteChangeDoc {

  constructor(readonly _id: string,
              readonly routeChange: RouteChange,
              readonly _rev: string) {
  }

  public static fromJSON(jsonObject: any): RouteChangeDoc {
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
