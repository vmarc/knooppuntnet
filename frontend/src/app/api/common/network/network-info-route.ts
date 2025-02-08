// this file is generated, please do not modify

import { Fact } from '@api/common/fact';
import { Day } from '@api/custom/day';
import { Timestamp } from '@api/custom/timestamp';

export interface NetworkInfoRoute {
  readonly id: number;
  readonly name: string;
  readonly wayCount: number;
  readonly length: number;
  readonly role: string;
  readonly relationLastUpdated: Timestamp;
  readonly lastUpdated: Timestamp;
  readonly lastSurvey?: Day;
  readonly facts: Fact[];
  readonly proposed: boolean;
}
