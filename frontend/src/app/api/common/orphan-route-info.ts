// this file is generated, please do not modify

import { Timestamp } from '@api/custom/timestamp';

export interface OrphanRouteInfo {
  readonly id: number;
  readonly name: string;
  readonly meters: number;
  readonly isBroken: boolean;
  readonly inaccessible: boolean;
  readonly lastSurvey?: string;
  readonly lastUpdated: Timestamp;
}
