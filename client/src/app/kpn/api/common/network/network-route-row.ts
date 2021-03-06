// this file is generated, please do not modify

import { Day } from '../../custom/day';
import { Timestamp } from '../../custom/timestamp';

export interface NetworkRouteRow {
  readonly id: number;
  readonly name: string;
  readonly length: number;
  readonly role: string;
  readonly investigate: boolean;
  readonly accessible: boolean;
  readonly roleConnection: boolean;
  readonly lastUpdated: Timestamp;
  readonly lastSurvey: Day;
  readonly proposed: boolean;
}
