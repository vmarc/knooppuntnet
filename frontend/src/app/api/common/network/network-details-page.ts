// this file is generated, please do not modify

import { NetworkFacts } from '@api/common/network-facts';
import { Tag } from '@api/custom/tag';
import { NetworkAttributes } from './network-attributes';
import { NetworkSummary } from './network-summary';

export interface NetworkDetailsPage {
  readonly summary: NetworkSummary;
  readonly active: boolean;
  readonly attributes: NetworkAttributes;
  readonly tags: Tag[];
  readonly facts: NetworkFacts;
}
