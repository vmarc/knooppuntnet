// this class is generated, please do not modify

import { ActionTimestamp } from './action-timestamp';
import { BarChart } from './bar-chart';

export class LogPage {
  constructor(
    readonly timestamp: ActionTimestamp,
    readonly periodType: string,
    readonly periodTitle: string,
    readonly previous: string,
    readonly next: string,
    readonly tile: BarChart,
    readonly tileRobot: BarChart,
    readonly api: BarChart,
    readonly apiRobot: BarChart,
    readonly analysis: BarChart,
    readonly analysisRobot: BarChart,
    readonly robot: BarChart,
    readonly nonRobot: BarChart
  ) {}

  static fromJSON(jsonObject: any): LogPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LogPage(
      ActionTimestamp.fromJSON(jsonObject.timestamp),
      jsonObject.periodType,
      jsonObject.periodTitle,
      jsonObject.previous,
      jsonObject.next,
      BarChart.fromJSON(jsonObject.tile),
      BarChart.fromJSON(jsonObject.tileRobot),
      BarChart.fromJSON(jsonObject.api),
      BarChart.fromJSON(jsonObject.apiRobot),
      BarChart.fromJSON(jsonObject.analysis),
      BarChart.fromJSON(jsonObject.analysisRobot),
      BarChart.fromJSON(jsonObject.robot),
      BarChart.fromJSON(jsonObject.nonRobot)
    );
  }
}
