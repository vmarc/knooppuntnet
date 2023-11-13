// this file is generated, please do not modify

import { Day } from '@api/custom';
import { Fact } from '@api/custom';
import { Timestamp } from '@api/custom';

export interface NetworkInfoRoute {
  readonly id: number;
  readonly name: string;
  readonly wayCount: number;
  readonly length: number;
  readonly role: string;
  readonly relationLastUpdated: Timestamp;
  readonly lastUpdated: Timestamp;
  readonly lastSurvey: Day;
  readonly facts: Fact[];
  readonly proposed: boolean;
}
