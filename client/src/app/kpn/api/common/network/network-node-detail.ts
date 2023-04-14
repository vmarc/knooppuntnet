// this file is generated, please do not modify

import { Day } from '@api/custom';
import { Fact } from '@api/custom';
import { Timestamp } from '@api/custom';

export interface NetworkNodeDetail {
  readonly id: number;
  readonly name: string;
  readonly longName: string;
  readonly latitude: string;
  readonly longitude: string;
  readonly connection: boolean;
  readonly roleConnection: boolean;
  readonly definedInRelation: boolean;
  readonly proposed: boolean;
  readonly timestamp: Timestamp;
  readonly lastSurvey: Day;
  readonly expectedRouteCount: number;
  readonly facts: Fact[];
}
