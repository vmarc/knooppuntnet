// this class is generated, please do not modify

import {List} from 'immutable';
import {NetworkExtraMemberNode} from './network-extra-member-node';
import {NetworkExtraMemberRelation} from './network-extra-member-relation';
import {NetworkExtraMemberWay} from './network-extra-member-way';
import {NetworkIntegrityCheck} from './network-integrity-check';
import {NetworkIntegrityCheckFailed} from './network-integrity-check-failed';
import {NetworkNameMissing} from './network-name-missing';

export class NetworkFacts {
  readonly networkExtraMemberNode: List<NetworkExtraMemberNode>;
  readonly networkExtraMemberWay: List<NetworkExtraMemberWay>;
  readonly networkExtraMemberRelation: List<NetworkExtraMemberRelation>;
  readonly integrityCheck: NetworkIntegrityCheck;
  readonly integrityCheckFailed: NetworkIntegrityCheckFailed;
  readonly nameMissing: NetworkNameMissing;

  constructor(networkExtraMemberNode: List<NetworkExtraMemberNode>,
              networkExtraMemberWay: List<NetworkExtraMemberWay>,
              networkExtraMemberRelation: List<NetworkExtraMemberRelation>,
              integrityCheck: NetworkIntegrityCheck,
              integrityCheckFailed: NetworkIntegrityCheckFailed,
              nameMissing: NetworkNameMissing) {
    this.networkExtraMemberNode = networkExtraMemberNode;
    this.networkExtraMemberWay = networkExtraMemberWay;
    this.networkExtraMemberRelation = networkExtraMemberRelation;
    this.integrityCheck = integrityCheck;
    this.integrityCheckFailed = integrityCheckFailed;
    this.nameMissing = nameMissing;
  }

  public static fromJSON(jsonObject): NetworkFacts {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkFacts(
      jsonObject.networkExtraMemberNode ? List(jsonObject.networkExtraMemberNode.map(json => NetworkExtraMemberNode.fromJSON(json))) : List(),
      jsonObject.networkExtraMemberWay ? List(jsonObject.networkExtraMemberWay.map(json => NetworkExtraMemberWay.fromJSON(json))) : List(),
      jsonObject.networkExtraMemberRelation ? List(jsonObject.networkExtraMemberRelation.map(json => NetworkExtraMemberRelation.fromJSON(json))) : List(),
      NetworkIntegrityCheck.fromJSON(jsonObject.integrityCheck),
      NetworkIntegrityCheckFailed.fromJSON(jsonObject.integrityCheckFailed),
      NetworkNameMissing.fromJSON(jsonObject.nameMissing)
    );
  }
}
