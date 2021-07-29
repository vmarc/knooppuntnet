// this file is generated, please do not modify

import { NetworkAttributes } from './network-attributes';
import { NetworkFacts } from '../network-facts';
import { NetworkSummary } from './network-summary';
import { Tags } from '../../custom/tags';

export interface NetworkDetailsPage {
  readonly summary: NetworkSummary;
  readonly active: boolean;
  readonly attributes: NetworkAttributes;
  readonly tags: Tags;
  readonly facts: NetworkFacts;
}
