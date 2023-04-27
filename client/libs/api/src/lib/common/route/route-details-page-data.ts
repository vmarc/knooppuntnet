// this file is generated, please do not modify

import { Day } from '@api/custom';
import { Fact } from '@api/custom';
import { Tags } from '@api/custom';
import { Timestamp } from '@api/custom';
import { RouteSummary } from '../route-summary';
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
  readonly tags: Tags;
  readonly facts: Fact[];
  readonly analysis: RouteInfoAnalysis;
  readonly tiles: string[];
  readonly nodeRefs: number[];
}
