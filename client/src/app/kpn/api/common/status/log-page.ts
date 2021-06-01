// this file is generated, please do not modify

import { ActionTimestamp } from './action-timestamp';
import { BarChart } from './bar-chart';

export interface LogPage {
  readonly timestamp: ActionTimestamp;
  readonly periodType: string;
  readonly periodTitle: string;
  readonly previous: string;
  readonly next: string;
  readonly tile: BarChart;
  readonly tileRobot: BarChart;
  readonly api: BarChart;
  readonly apiRobot: BarChart;
  readonly analysis: BarChart;
  readonly analysisRobot: BarChart;
  readonly robot: BarChart;
  readonly nonRobot: BarChart;
}
