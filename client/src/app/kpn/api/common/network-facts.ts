// this file is generated, please do not modify

import { NetworkExtraMemberNode } from './network-extra-member-node';
import { NetworkExtraMemberRelation } from './network-extra-member-relation';
import { NetworkExtraMemberWay } from './network-extra-member-way';
import { NetworkIntegrityCheck } from './network-integrity-check';
import { NetworkIntegrityCheckFailed } from './network-integrity-check-failed';
import { NetworkNameMissing } from './network-name-missing';

export interface NetworkFacts {
  readonly networkExtraMemberNode: NetworkExtraMemberNode[] | undefined;
  readonly networkExtraMemberWay: NetworkExtraMemberWay[] | undefined;
  readonly networkExtraMemberRelation: NetworkExtraMemberRelation[] | undefined;
  readonly integrityCheck: NetworkIntegrityCheck;
  readonly integrityCheckFailed: NetworkIntegrityCheckFailed;
  readonly nameMissing: NetworkNameMissing;
}
