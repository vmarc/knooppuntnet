// this class is generated, please do not modify

import {NetworkType} from "../../custom/network-type";

export class NetworkSummary {

  constructor(readonly networkType: NetworkType,
              readonly name: string,
              readonly factCount: number,
              readonly nodeCount: number,
              readonly routeCount: number,
              readonly changeCount: number,
              readonly active: boolean) {
  }

  public static fromJSON(jsonObject: any): NetworkSummary {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkSummary(
      NetworkType.fromJSON(jsonObject.networkType),
      jsonObject.name,
      jsonObject.factCount,
      jsonObject.nodeCount,
      jsonObject.routeCount,
      jsonObject.changeCount,
      jsonObject.active
    );
  }
}
