// this file is generated, please do not modify

import { Day } from '../../custom/day';
import { Fact } from '../../custom/fact';
import { Timestamp } from '../../custom/timestamp';

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
