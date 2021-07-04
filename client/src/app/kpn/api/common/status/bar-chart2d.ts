// this class is generated, please do not modify

import { List } from 'immutable';
import { BarChart2dValue } from './bar-chart2d-value';

export class BarChart2D {
  constructor(
    readonly xAxisLabel: string,
    readonly yAxisLabel: string,
    readonly xAxisTicks: List<number>,
    readonly legendTitle: string,
    readonly data: List<BarChart2dValue>
  ) {}

  public static fromJSON(jsonObject: any): BarChart2D {
    if (!jsonObject) {
      return undefined;
    }
    return new BarChart2D(
      jsonObject.xAxisLabel,
      jsonObject.yAxisLabel,
      jsonObject.xAxisTicks ? List(jsonObject.xAxisTicks) : List(),
      jsonObject.legendTitle,
      jsonObject.data
        ? List(
            jsonObject.data.map((json: any) => BarChart2dValue.fromJSON(json))
          )
        : List()
    );
  }
}
