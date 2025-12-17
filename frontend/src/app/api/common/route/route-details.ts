// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { Fact } from '@api/common/fact';
import { RouteSummary } from '@api/common/route-summary';
import { Reference } from '@api/common/common/reference';
import { Raw } from '@api/common/data/raw/raw';
import { LocationCandidateInfo } from '@api/common/location/location-candidate-info';
import { Day } from '@api/custom/day';
import { Timestamp } from '@api/custom/timestamp';
import { ParentRoute } from './parent-route';
import { RouteNodes } from './route-nodes';

export interface RouteDetails {
  readonly id: number;
  readonly active: boolean;
  readonly raw: Raw;
  readonly summary: RouteSummary;
  readonly proposed: boolean;
  readonly lastUpdated: Timestamp;
  readonly lastSurvey?: Day;
  readonly facts: Fact[];
  readonly unexpectedNodeIds: number[];
  readonly unexpectedRelationIds: number[];
  readonly memberCount: number;
  readonly segmentCount: number;
  readonly pathCount: number;
  readonly nameDerivedFromNodes: boolean;
  readonly nodes: RouteNodes;
  readonly bounds?: Bounds;
  readonly routeIds: number[];
  readonly relationCount: number;
  readonly relationLevels: number;
  readonly parentRoutes: ParentRoute[];
  readonly networkReferences: Reference[];
  readonly locationCandidateInfos: LocationCandidateInfo[];
}
