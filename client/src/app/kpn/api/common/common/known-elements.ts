// this class is generated, please do not modify

import {List} from "immutable";

export class KnownElements {

  constructor(readonly nodeIds: List<number>,
              readonly routeIds: List<number>) {
  }

  public static fromJSON(jsonObject): KnownElements {
    if (!jsonObject) {
      return undefined;
    }
    return new KnownElements(
      jsonObject.nodeIds ? List(jsonObject.nodeIds) : List(),
      jsonObject.routeIds ? List(jsonObject.routeIds) : List()
    );
  }
}
