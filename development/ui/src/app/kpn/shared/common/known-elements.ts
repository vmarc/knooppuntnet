// this class is generated, please do not modify

import {List} from 'immutable';

export class KnownElements {
  readonly nodeIds: List<number>;
  readonly routeIds: List<number>;

  constructor(nodeIds: List<number>,
              routeIds: List<number>) {
    this.nodeIds = nodeIds;
    this.routeIds = routeIds;
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
