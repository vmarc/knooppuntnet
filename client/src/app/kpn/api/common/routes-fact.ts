// this class is generated, please do not modify

import {List} from "immutable";
import {Ref} from "./common/ref";

export class RoutesFact {

  constructor(readonly routes: List<Ref>) {
  }

  public static fromJSON(jsonObject: any): RoutesFact {
    if (!jsonObject) {
      return undefined;
    }
    return new RoutesFact(
      jsonObject.routes ? List(jsonObject.routes.map(json => Ref.fromJSON(json))) : List()
    );
  }
}
