// this file is generated, please do not modify

import { LocationInfo } from '@api/common';
import { LocationSummary } from './location-summary';

export interface LocationDetailsPage {
  readonly summary: LocationSummary;
  readonly distance: number;
  readonly locationInfos: LocationInfo[];
}
