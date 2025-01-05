// this file is generated, please do not modify

import { Bounds } from '@api/common';
import { Fact } from '@api/common';
import { RouteSummary } from '@api/common';
import { LocationCandidateInfo } from '@api/common/location';
import { Day } from '@api/custom';
import { Timestamp } from '@api/custom';
import { ParentRoute } from './parent-route';
import { RouteNodes } from './route-nodes';
import { RouteStructureRow } from './route-structure-row';

export interface RouteDetailsPageData {
  readonly id: number;
  readonly active: boolean;
  readonly summary: RouteSummary;
  readonly proposed: boolean;
  readonly version: number;
  readonly changeSetId: number;
  readonly lastUpdated: Timestamp;
  readonly lastSurvey?: Day;
  readonly facts: Fact[];
  readonly locationCandidateInfos: LocationCandidateInfo[];
  readonly unexpectedNodeIds: number[];
  readonly unexpectedRelationIds: number[];
  readonly structureRows: RouteStructureRow[];
  readonly nameDerivedFromNodes: boolean;
  readonly nodes: RouteNodes;
  readonly bounds?: Bounds;
  readonly routeIds: number[];
  readonly parentRoutes: ParentRoute[];
}
