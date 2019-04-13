// this class is generated, please do not modify

import {LatLonImpl} from "../lat-lon-impl";

export class RouteLegNode {

  constructor(readonly nodeId: string,
              readonly nodeName: string,
              readonly latLon: LatLonImpl) {
  }

  public static fromJSON(jsonObject): RouteLegNode {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteLegNode(
      jsonObject.nodeId,
      jsonObject.nodeName,
      LatLonImpl.fromJSON(jsonObject.latLon)
    );
  }
}
