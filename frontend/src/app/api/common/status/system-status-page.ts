// this file is generated, please do not modify

import { ActionTimestamp } from './action-timestamp';
import { BarChart } from './bar-chart';

export interface SystemStatusPage {
  readonly timestamp: ActionTimestamp;
  readonly periodType: string;
  readonly periodTitle: string;
  readonly previous: string;
  readonly next: string;
  readonly backendDiskSpaceUsed: BarChart;
  readonly backendDiskSpaceAvailable: BarChart;
  readonly backendDiskSpaceOverpass: BarChart;
  readonly analysisDocCount: BarChart;
  readonly analysisDiskSize: BarChart;
  readonly analysisDiskSizeExternal: BarChart;
  readonly analysisDataSize: BarChart;
  readonly changesDocCount: BarChart;
  readonly changesDiskSize: BarChart;
  readonly changesDiskSizeExternal: BarChart;
  readonly changesDataSize: BarChart;
}
