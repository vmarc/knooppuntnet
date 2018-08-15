// this class is generated, please do not modify

import {Ref} from './common/ref';

export class RoutesFact {

  constructor(public routes?: Array<Ref>) {
  }

  public static fromJSON(jsonObject): RoutesFact {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RoutesFact();
    instance.routes = jsonObject.routes ? jsonObject.routes.map(json => Ref.fromJSON(json)) : [];
    return instance;
  }
}

