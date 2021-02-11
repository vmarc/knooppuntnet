// this class is generated, please do not modify

import {Bounds} from '../bounds';
import {NetworkMapNode} from './network-map-node';
import {NetworkSummary} from './network-summary';

export class NetworkMapPage {

  constructor(readonly networkSummary: NetworkSummary,
              readonly nodes: Array<NetworkMapNode>,
              readonly nodeIds: Array<number>,
              readonly routeIds: Array<number>,
              readonly bounds: Bounds) {
  }

  public static fromJSON(jsonObject: any): NetworkMapPage {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkMapPage(
      NetworkSummary.fromJSON(jsonObject.networkSummary),
      jsonObject.nodes.map((json: any) => NetworkMapNode.fromJSON(json)),
      jsonObject.nodeIds,
      jsonObject.routeIds,
      Bounds.fromJSON(jsonObject.bounds)
    );
  }
}
