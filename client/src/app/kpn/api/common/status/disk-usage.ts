// this class is generated, please do not modify

import { BarChart } from './bar-chart';

export class DiskUsage {
  constructor(
    readonly frontend: BarChart,
    readonly database: BarChart,
    readonly backend: BarChart
  ) {}

  public static fromJSON(jsonObject: any): DiskUsage {
    if (!jsonObject) {
      return undefined;
    }
    return new DiskUsage(
      BarChart.fromJSON(jsonObject.frontend),
      BarChart.fromJSON(jsonObject.database),
      BarChart.fromJSON(jsonObject.backend)
    );
  }
}
