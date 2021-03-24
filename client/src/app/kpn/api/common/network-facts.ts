// this class is generated, please do not modify

import {NetworkExtraMemberNode} from './network-extra-member-node';
import {NetworkExtraMemberRelation} from './network-extra-member-relation';
import {NetworkExtraMemberWay} from './network-extra-member-way';
import {NetworkIntegrityCheck} from './network-integrity-check';
import {NetworkIntegrityCheckFailed} from './network-integrity-check-failed';
import {NetworkNameMissing} from './network-name-missing';

export class NetworkFacts {

  constructor(readonly networkExtraMemberNode: Array<NetworkExtraMemberNode> | undefined,
              readonly networkExtraMemberWay: Array<NetworkExtraMemberWay> | undefined,
              readonly networkExtraMemberRelation: Array<NetworkExtraMemberRelation> | undefined,
              readonly integrityCheck: NetworkIntegrityCheck | undefined,
              readonly integrityCheckFailed: NetworkIntegrityCheckFailed | undefined,
              readonly nameMissing: NetworkNameMissing | undefined) {
  }

  static fromJSON(jsonObject: any): NetworkFacts {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkFacts(
      jsonObject.networkExtraMemberNode?.map((json: any) => NetworkExtraMemberNode.fromJSON(json)),
      jsonObject.networkExtraMemberWay?.map((json: any) => NetworkExtraMemberWay.fromJSON(json)),
      jsonObject.networkExtraMemberRelation?.map((json: any) => NetworkExtraMemberRelation.fromJSON(json)),
      NetworkIntegrityCheck.fromJSON(jsonObject.integrityCheck),
      NetworkIntegrityCheckFailed.fromJSON(jsonObject.integrityCheckFailed),
      NetworkNameMissing.fromJSON(jsonObject.nameMissing)
    );
  }
}
