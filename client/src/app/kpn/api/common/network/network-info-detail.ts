// this class is generated, please do not modify

import { List } from 'immutable';
import { NetworkFacts } from '../network-facts';
import { NetworkInfoNode } from './network-info-node';
import { NetworkInfoRoute } from './network-info-route';
import { NetworkShape } from './network-shape';

export class NetworkInfoDetail {
  constructor(
    readonly nodes: List<NetworkInfoNode>,
    readonly routes: List<NetworkInfoRoute>,
    readonly networkFacts: NetworkFacts,
    readonly shape: NetworkShape
  ) {}

  public static fromJSON(jsonObject: any): NetworkInfoDetail {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkInfoDetail(
      jsonObject.nodes
        ? List(
            jsonObject.nodes.map((json: any) => NetworkInfoNode.fromJSON(json))
          )
        : List(),
      jsonObject.routes
        ? List(
            jsonObject.routes.map((json: any) =>
              NetworkInfoRoute.fromJSON(json)
            )
          )
        : List(),
      NetworkFacts.fromJSON(jsonObject.networkFacts),
      NetworkShape.fromJSON(jsonObject.shape)
    );
  }
}
