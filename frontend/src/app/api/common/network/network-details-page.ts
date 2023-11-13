// this file is generated, please do not modify

import { NetworkFacts } from '@api/common';
import { Tags } from '@api/custom';
import { NetworkAttributes } from './network-attributes';
import { NetworkSummary } from './network-summary';

export interface NetworkDetailsPage {
  readonly summary: NetworkSummary;
  readonly active: boolean;
  readonly attributes: NetworkAttributes;
  readonly tags: Tags;
  readonly facts: NetworkFacts;
}
