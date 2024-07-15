// this file is generated, please do not modify

import { RouteLocationAnalysis } from '@api/common';
import { RouteMap } from './route-map';

export interface RouteInfoAnalysis {
  readonly expectedName: string;
  readonly map: RouteMap;
  readonly structureStrings: string[];
  readonly geometryDigest: string;
  readonly locationAnalysis: RouteLocationAnalysis;
}
