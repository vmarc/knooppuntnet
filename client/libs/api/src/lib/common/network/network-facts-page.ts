// this file is generated, please do not modify

import { NetworkFact } from '@api/common';
import { NetworkSummary } from './network-summary';

export interface NetworkFactsPage {
  readonly _id: number;
  readonly summary: NetworkSummary;
  readonly facts: NetworkFact[];
}
