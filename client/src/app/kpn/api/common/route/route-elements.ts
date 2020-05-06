// this class is generated, please do not modify

import {ElementIds} from "../../../server/analyzer/engine/changes/changes/element-ids";

export class RouteElements {

  constructor(readonly routeId: number,
              readonly elementIds: ElementIds) {
  }

  public static fromJSON(jsonObject: any): RouteElements {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteElements(
      jsonObject.routeId,
      ElementIds.fromJSON(jsonObject.elementIds)
    );
  }
}
