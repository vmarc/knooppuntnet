// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { Country } from '@api/common/country';
import { Fact } from '@api/common/fact';
import { RouteScope } from '@api/common/route-scope';
import { RouteType } from '@api/common/route-type';
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
  readonly countries: Country[];
  readonly nodeNetwork: boolean;
  readonly routeTypes: RouteType[];
  readonly scopes: RouteScope[];
  readonly name: string;
  readonly meters: number;
  readonly wayCount: number;
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
