// this class is generated, please do not modify

import {NetworkType} from "../network-type";

export class NetworkSummary {

  constructor(readonly networkType: NetworkType,
              readonly name: string,
              readonly factCount: number,
              readonly nodeCount: number,
              readonly routeCount: number) {
  }

  public static fromJSON(jsonObject): NetworkSummary {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkSummary(
      NetworkType.fromJSON(jsonObject.networkType),
      jsonObject.name,
      jsonObject.factCount,
      jsonObject.nodeCount,
      jsonObject.routeCount
    );
  }
}
