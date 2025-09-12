// this file is generated, please do not modify

import { Timestamp } from '@api/custom/timestamp';
import { Fact } from './fact';

export interface OrphanRouteInfo {
  readonly id: number;
  readonly name: string;
  readonly meters: number;
  readonly isBroken: boolean;
  readonly lastSurvey?: string;
  readonly lastUpdated: Timestamp;
  readonly facts: Fact[];
}
