// this class is generated, please do not modify

import {ViaRoute} from "./via-route";

export class LegBuildParams {

  constructor(readonly networkType: string,
              readonly legId: string,
              readonly sourceNodeId: number,
              readonly sinkNodeId: number,
              readonly viaRoute: ViaRoute) {
  }

  public static fromJSON(jsonObject: any): LegBuildParams {
    if (!jsonObject) {
      return undefined;
    }
    return new LegBuildParams(
      jsonObject.networkType,
      jsonObject.legId,
      jsonObject.sourceNodeId,
      jsonObject.sinkNodeId,
      ViaRoute.fromJSON(jsonObject.viaRoute)
    );
  }
}
