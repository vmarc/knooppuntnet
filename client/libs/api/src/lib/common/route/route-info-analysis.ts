// this file is generated, please do not modify

import { RouteLocationAnalysis } from '@api/common';
import { RouteMemberInfo } from '@api/custom';
import { RouteMap } from './route-map';

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
