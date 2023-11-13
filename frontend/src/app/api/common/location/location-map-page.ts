// this file is generated, please do not modify

import { Bounds } from '@api/common';
import { LocationSummary } from './location-summary';

export interface LocationMapPage {
  readonly summary: LocationSummary;
  readonly bounds: Bounds;
  readonly geoJson: string;
}
