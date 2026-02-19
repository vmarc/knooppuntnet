// this file is generated, please do not modify

import { Country } from '@api/common/country';
import { Tag } from '@api/custom/tag';
import { NetworkDetail } from './network-detail';
import { NetworkSummary } from './network-summary';

export interface NetworkDetailsPage {
  readonly summary: NetworkSummary;
  readonly active: boolean;
  readonly country?: Country;
  readonly detail: NetworkDetail;
  readonly networkNodeIds: ReadonlyArray<number>;
  readonly connectionNodeIds: ReadonlyArray<number>;
  readonly networkRouteIds: ReadonlyArray<number>;
  readonly connectionRouteIds: ReadonlyArray<number>;
  readonly tags: ReadonlyArray<Tag>;
}
