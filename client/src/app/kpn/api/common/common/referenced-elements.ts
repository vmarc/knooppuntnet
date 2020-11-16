// this class is generated, please do not modify

import {List} from 'immutable';

export class ReferencedElements {

  constructor(readonly nodeIds: List<number>,
              readonly routeIds: List<number>) {
  }

  public static fromJSON(jsonObject: any): ReferencedElements {
    if (!jsonObject) {
      return undefined;
    }
    return new ReferencedElements(
      jsonObject.nodeIds ? List(jsonObject.nodeIds) : List(),
      jsonObject.routeIds ? List(jsonObject.routeIds) : List()
    );
  }
}
