// this class is generated, please do not modify

import {NetworkExtraMemberNode} from './network-extra-member-node';
import {NetworkExtraMemberRelation} from './network-extra-member-relation';
import {NetworkExtraMemberWay} from './network-extra-member-way';
import {NetworkIntegrityCheck} from './network-integrity-check';
import {NetworkIntegrityCheckFailed} from './network-integrity-check-failed';

export class NetworkFacts {

  constructor(public networkExtraMemberNode?: Array<NetworkExtraMemberNode>,
              public networkExtraMemberWay?: Array<NetworkExtraMemberWay>,
              public networkExtraMemberRelation?: Array<NetworkExtraMemberRelation>,
              public integrityCheck?: NetworkIntegrityCheck,
              public integrityCheckFailed?: NetworkIntegrityCheckFailed) {
  }

  public static fromJSON(jsonObject): NetworkFacts {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkFacts();
    instance.networkExtraMemberNode = jsonObject.networkExtraMemberNode ? jsonObject.networkExtraMemberNode.map(json => NetworkExtraMemberNode.fromJSON(json)) : [];
    instance.networkExtraMemberWay = jsonObject.networkExtraMemberWay ? jsonObject.networkExtraMemberWay.map(json => NetworkExtraMemberWay.fromJSON(json)) : [];
    instance.networkExtraMemberRelation = jsonObject.networkExtraMemberRelation ? jsonObject.networkExtraMemberRelation.map(json => NetworkExtraMemberRelation.fromJSON(json)) : [];
    instance.integrityCheck = jsonObject.integrityCheck;
    instance.integrityCheckFailed = jsonObject.integrityCheckFailed;
    return instance;
  }
}

