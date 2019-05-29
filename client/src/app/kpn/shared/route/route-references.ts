// this class is generated, please do not modify

import {List} from "immutable";
import {Reference} from "../common/reference";

export class RouteReferences {

  constructor(readonly networkReferences: List<Reference>) {
  }

  public static fromJSON(jsonObject): RouteReferences {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteReferences(
      jsonObject.networkReferences ? List(jsonObject.networkReferences.map(json => Reference.fromJSON(json))) : List()
    );
  }
}
