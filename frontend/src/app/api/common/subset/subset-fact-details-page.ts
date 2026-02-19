// this file is generated, please do not modify

import { Fact } from '@api/common/fact';
import { NetworkFactRefs } from './network-fact-refs';
import { SubsetInfo } from './subset-info';

export interface SubsetFactDetailsPage {
  readonly subsetInfo: SubsetInfo;
  readonly fact: Fact;
  readonly networks: ReadonlyArray<NetworkFactRefs>;
}
