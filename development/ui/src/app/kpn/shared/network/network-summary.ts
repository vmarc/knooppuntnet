// this class is generated, please do not modify

import {NetworkType} from '../network-type';

export class NetworkSummary {
  readonly networkType: NetworkType;
  readonly name: string;
  readonly factCount: number;
  readonly nodeCount: number;
  readonly routeCount: number;

  constructor(networkType: NetworkType,
              name: string,
              factCount: number,
              nodeCount: number,
              routeCount: number) {
    this.networkType = networkType;
    this.name = name;
    this.factCount = factCount;
    this.nodeCount = nodeCount;
    this.routeCount = routeCount;
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
