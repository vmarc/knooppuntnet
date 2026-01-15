// this file is generated, please do not modify

import { Fact } from '@api/common/fact';
import { Day } from '@api/custom/day';
import { Timestamp } from '@api/custom/timestamp';

export interface NetworkRouteRow {
  readonly id: number;
  readonly name: string;
  readonly length: number;
  readonly role?: string;
  readonly investigate: boolean;
  readonly accessible: boolean;
  readonly roleConnection: boolean;
  readonly lastUpdated: Timestamp;
  readonly lastSurvey?: Day;
  readonly proposed: boolean;
  readonly facts: Fact[];
  readonly symbol?: string;
}
