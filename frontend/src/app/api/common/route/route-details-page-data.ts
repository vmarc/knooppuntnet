// this file is generated, please do not modify

import { RouteSummary } from '@api/common';
import { LocationCandidateInfo } from '@api/common/location';
import { Day } from '@api/custom';
import { Fact } from '@api/custom';
import { Timestamp } from '@api/custom';
import { RouteInfoAnalysis } from './route-info-analysis';

export interface RouteDetailsPageData {
  readonly id: number;
  readonly active: boolean;
  readonly summary: RouteSummary;
  readonly proposed: boolean;
  readonly version: number;
  readonly changeSetId: number;
  readonly lastUpdated: Timestamp;
  readonly lastSurvey: Day;
  readonly facts: Fact[];
  readonly locationCandidateInfos: LocationCandidateInfo[];
  readonly analysis: RouteInfoAnalysis;
  readonly tiles: string[];
  readonly nodeRefs: number[];
}
