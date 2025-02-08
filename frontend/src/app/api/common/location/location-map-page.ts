// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { LocationSummary } from './location-summary';

export interface LocationMapPage {
  readonly summary: LocationSummary;
  readonly bounds: Bounds;
  readonly geoJson: string;
  readonly geoJson2: string;
}
