// this file is generated, please do not modify

import { Fact } from '@api/custom';
import { Tags } from '@api/custom';
import { NetworkAttributes } from './network-attributes';
import { NetworkInfoDetail } from './network-info-detail';

export interface NetworkInfo {
  readonly _id: number;
  readonly attributes: NetworkAttributes;
  readonly active: boolean;
  readonly nodeRefs: number[];
  readonly routeRefs: number[];
  readonly networkRefs: number[];
  readonly facts: Fact[];
  readonly tags: Tags;
  readonly detail: NetworkInfoDetail;
}
