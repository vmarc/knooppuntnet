// this class is generated, please do not modify

import { NetworkFacts } from '../network-facts';
import { NetworkInfoNode } from './network-info-node';
import { NetworkInfoRoute } from './network-info-route';
import { NetworkShape } from './network-shape';

export class NetworkInfoDetail {
  constructor(
    readonly nodes: Array<NetworkInfoNode>,
    readonly routes: Array<NetworkInfoRoute>,
    readonly networkFacts: NetworkFacts,
    readonly shape: NetworkShape
  ) {}

  static fromJSON(jsonObject: any): NetworkInfoDetail {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkInfoDetail(
      jsonObject.nodes.map((json: any) => NetworkInfoNode.fromJSON(json)),
      jsonObject.routes.map((json: any) => NetworkInfoRoute.fromJSON(json)),
      NetworkFacts.fromJSON(jsonObject.networkFacts),
      NetworkShape.fromJSON(jsonObject.shape)
    );
  }
}
