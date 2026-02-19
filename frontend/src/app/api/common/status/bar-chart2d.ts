// this file is generated, please do not modify

import { BarChart2dValue } from './bar-chart2d-value';

export interface BarChart2D {
  readonly xAxisLabel: string;
  readonly yAxisLabel: string;
  readonly xAxisTicks: ReadonlyArray<number>;
  readonly legendTitle: string;
  readonly data: ReadonlyArray<BarChart2dValue>;
}
