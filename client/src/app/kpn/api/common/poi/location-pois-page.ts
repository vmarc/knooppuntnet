// this file is generated, please do not modify

import { LocationPoiInfo } from './location-poi-info';
import { TimeInfo } from '../time-info';

export interface LocationPoisPage {
  readonly timeInfo: TimeInfo;
  readonly poiCount: number;
  readonly pois: LocationPoiInfo[];
}
