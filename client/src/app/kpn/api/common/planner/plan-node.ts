// this class is generated, please do not modify

import {LatLonImpl} from "../lat-lon-impl";

export class PlanNode {

  constructor(readonly featureId: string,
              readonly nodeId: string,
              readonly nodeName: string,
              readonly coordinate: Array<number>,
              readonly latLon: LatLonImpl) {
  }

  public static fromJSON(jsonObject: any): PlanNode {
    if (!jsonObject) {
      return undefined;
    }
    return new PlanNode(
      jsonObject.featureId,
      jsonObject.nodeId,
      jsonObject.nodeName,
      jsonObject.coordinate ? Array(jsonObject.coordinate) : Array(),
      LatLonImpl.fromJSON(jsonObject.latLon)
    );
  }
}
