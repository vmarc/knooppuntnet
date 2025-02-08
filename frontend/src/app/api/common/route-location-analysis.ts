// this file is generated, please do not modify

import { Location } from '@api/common/location/location';
import { LocationCandidate } from '@api/common/location/location-candidate';

export interface RouteLocationAnalysis {
  readonly location?: Location;
  readonly candidates: LocationCandidate[];
  readonly locationNames: string[];
}
