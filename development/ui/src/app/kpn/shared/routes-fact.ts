// this class is generated, please do not modify

import {List} from 'immutable';
import {Ref} from './common/ref';

export class RoutesFact {
  readonly routes: List<Ref>;

  constructor(routes: List<Ref>) {
    this.routes = routes;
  }

  public static fromJSON(jsonObject): RoutesFact {
    if (!jsonObject) {
      return undefined;
    }
    return new RoutesFact(
      jsonObject.routes ? List(jsonObject.routes.map(json => Ref.fromJSON(json))) : List()
    );
  }
}
