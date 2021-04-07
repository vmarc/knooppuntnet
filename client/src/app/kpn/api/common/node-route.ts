// this class is generated, please do not modify

import { NetworkType } from '../custom/network-type';

export class NodeRoute {
  constructor(
    readonly id: number,
    readonly name: string,
    readonly networkType: NetworkType,
    readonly locationNames: Array<string>,
    readonly expectedRouteCount: number,
    readonly actualRouteCount: number
  ) {}

  static fromJSON(jsonObject: any): NodeRoute {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeRoute(
      jsonObject.id,
      jsonObject.name,
      jsonObject.networkType,
      jsonObject.locationNames,
      jsonObject.expectedRouteCount,
      jsonObject.actualRouteCount
    );
  }
}
