// this class is generated, please do not modify

import {BarChart2dValue} from './bar-chart2d-value';

export class BarChart2D {

  constructor(readonly xAxisLabel: string,
              readonly yAxisLabel: string,
              readonly xAxisTicks: Array<number>,
              readonly legendTitle: string,
              readonly data: Array<BarChart2dValue>) {
  }

  public static fromJSON(jsonObject: any): BarChart2D {
    if (!jsonObject) {
      return undefined;
    }
    return new BarChart2D(
      jsonObject.xAxisLabel,
      jsonObject.yAxisLabel,
      jsonObject.xAxisTicks ? Array(jsonObject.xAxisTicks) : Array(),
      jsonObject.legendTitle,
      jsonObject.data ? Array(jsonObject.data.map((json: any) => BarChart2dValue.fromJSON(json))) : Array()
    );
  }
}
