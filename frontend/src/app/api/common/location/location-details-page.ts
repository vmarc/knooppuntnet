// this file is generated, please do not modify

import { LocationInfo } from '@api/common';
import { Tags } from '@api/custom';
import { LocationSummary } from './location-summary';

export interface LocationDetailsPage {
  readonly summary: LocationSummary;
  readonly relationId: number;
  readonly distance: number;
  readonly locationInfos: LocationInfo[];
  readonly tags: Tags;
}
