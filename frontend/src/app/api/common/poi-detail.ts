// this file is generated, please do not modify

import { Poi } from '@api/common/poi';
import { PoiAnalysis } from './poi-analysis';
import { PoiState } from './poi-state';

export interface PoiDetail {
  readonly poi: Poi;
  readonly poiAnalysis: PoiAnalysis;
  readonly poiState: PoiState;
}
