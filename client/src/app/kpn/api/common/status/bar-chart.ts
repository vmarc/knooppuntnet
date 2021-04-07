// this class is generated, please do not modify

import { NameValue } from './name-value';

export class BarChart {
  constructor(
    readonly xAxisTicks: Array<number>,
    readonly data: Array<NameValue>
  ) {}

  static fromJSON(jsonObject: any): BarChart {
    if (!jsonObject) {
      return undefined;
    }
    return new BarChart(
      jsonObject.xAxisTicks,
      jsonObject.data?.map((json: any) => NameValue.fromJSON(json))
    );
  }
}
