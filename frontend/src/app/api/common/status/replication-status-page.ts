// this file is generated, please do not modify

import { ActionTimestamp } from './action-timestamp';
import { BarChart } from './bar-chart';
import { BarChart2D } from './bar-chart2d';

export interface ReplicationStatusPage {
  readonly timestamp: ActionTimestamp;
  readonly periodType: string;
  readonly periodTitle: string;
  readonly previous: string;
  readonly next: string;
  readonly delay: BarChart2D;
  readonly analysisDelay: BarChart;
  readonly updateDelay: BarChart;
  readonly replicationDelay: BarChart;
  readonly replicationBytes: BarChart;
  readonly replicationElements: BarChart;
  readonly replicationChangeSets: BarChart;
}
