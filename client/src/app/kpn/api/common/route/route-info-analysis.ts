// this file is generated, please do not modify

import { RouteLocationAnalysis } from '../route-location-analysis';
import { RouteMap } from './route-map';
import { RouteMemberInfo } from '../../custom/route-member-info';

export interface RouteInfoAnalysis {
  readonly unexpectedNodeIds: number[];
  readonly members: RouteMemberInfo[];
  readonly expectedName: string;
  readonly map: RouteMap;
  readonly structureStrings: string[];
  readonly geometryDigest: string;
  readonly locationAnalysis: RouteLocationAnalysis;
}
