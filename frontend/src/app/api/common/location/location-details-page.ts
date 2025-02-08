// this file is generated, please do not modify

import { LocationInfo } from '@api/common/location-info';
import { Tag } from '@api/custom/tag';
import { LocationSummary } from './location-summary';

export interface LocationDetailsPage {
  readonly summary: LocationSummary;
  readonly relationId: number;
  readonly distance: number;
  readonly locationInfos: LocationInfo[];
  readonly tags: Tag[];
}
