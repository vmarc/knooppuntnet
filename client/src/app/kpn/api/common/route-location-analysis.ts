// this file is generated, please do not modify

import { Location } from './location/location';
import { LocationCandidate } from './location/location-candidate';

export interface RouteLocationAnalysis {
  readonly location: Location;
  readonly candidates: LocationCandidate[];
  readonly locationNames: string[];
}
