// this class is generated, please do not modify

import {List} from 'immutable';

export class ReferencedElements {
  readonly nodeIds: List<number>;
  readonly routeIds: List<number>;

  constructor(nodeIds: List<number>,
              routeIds: List<number>) {
    this.nodeIds = nodeIds;
    this.routeIds = routeIds;
  }

  public static fromJSON(jsonObject): ReferencedElements {
    if (!jsonObject) {
      return undefined;
    }
    return new ReferencedElements(
      jsonObject.nodeIds ? List(jsonObject.nodeIds) : List(),
      jsonObject.routeIds ? List(jsonObject.routeIds) : List()
    );
  }
}
