// this class is generated, please do not modify

import { List } from 'immutable';
import { NetworkExtraMemberNode } from './network-extra-member-node';
import { NetworkExtraMemberRelation } from './network-extra-member-relation';
import { NetworkExtraMemberWay } from './network-extra-member-way';
import { NetworkIntegrityCheck } from './network-integrity-check';
import { NetworkIntegrityCheckFailed } from './network-integrity-check-failed';
import { NetworkNameMissing } from './network-name-missing';

export class NetworkFacts {
  constructor(
    readonly networkExtraMemberNode: List<NetworkExtraMemberNode>,
    readonly networkExtraMemberWay: List<NetworkExtraMemberWay>,
    readonly networkExtraMemberRelation: List<NetworkExtraMemberRelation>,
    readonly integrityCheck: NetworkIntegrityCheck,
    readonly integrityCheckFailed: NetworkIntegrityCheckFailed,
    readonly nameMissing: NetworkNameMissing
  ) {}

  public static fromJSON(jsonObject: any): NetworkFacts {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkFacts(
      jsonObject.networkExtraMemberNode
        ? List(
            jsonObject.networkExtraMemberNode.map((json: any) =>
              NetworkExtraMemberNode.fromJSON(json)
            )
          )
        : List(),
      jsonObject.networkExtraMemberWay
        ? List(
            jsonObject.networkExtraMemberWay.map((json: any) =>
              NetworkExtraMemberWay.fromJSON(json)
            )
          )
        : List(),
      jsonObject.networkExtraMemberRelation
        ? List(
            jsonObject.networkExtraMemberRelation.map((json: any) =>
              NetworkExtraMemberRelation.fromJSON(json)
            )
          )
        : List(),
      NetworkIntegrityCheck.fromJSON(jsonObject.integrityCheck),
      NetworkIntegrityCheckFailed.fromJSON(jsonObject.integrityCheckFailed),
      NetworkNameMissing.fromJSON(jsonObject.nameMissing)
    );
  }
}
