// this file is generated, please do not modify

import { RouteLocationAnalysis } from '../route-location-analysis';
import { RouteMap } from './route-map';
import { RouteMemberInfo } from '@api/custom/route-member-info';

export interface RouteInfoAnalysis {
  readonly unexpectedNodeIds: number[];
  readonly unexpectedRelationIds: number[];
  readonly members: RouteMemberInfo[];
  readonly expectedName: string;
  readonly nameDerivedFromNodes: boolean;
  readonly map: RouteMap;
  readonly structureStrings: string[];
  readonly geometryDigest: string;
  readonly locationAnalysis: RouteLocationAnalysis;
}
