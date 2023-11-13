// this file is generated, please do not modify

import { Location } from '@api/common/location';
import { LocationCandidate } from '@api/common/location';

export interface RouteLocationAnalysis {
  readonly location: Location;
  readonly candidates: LocationCandidate[];
  readonly locationNames: string[];
}
