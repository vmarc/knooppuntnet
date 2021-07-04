// this class is generated, please do not modify

import { ActionTimestamp } from './action-timestamp';
import { BarChart } from './bar-chart';
import { BarChart2D } from './bar-chart2d';

export class ReplicationStatusPage {
  constructor(
    readonly timestamp: ActionTimestamp,
    readonly periodType: string,
    readonly periodTitle: string,
    readonly previous: string,
    readonly next: string,
    readonly delay: BarChart2D,
    readonly analysisDelay: BarChart,
    readonly updateDelay: BarChart,
    readonly replicationDelay: BarChart,
    readonly replicationBytes: BarChart,
    readonly replicationElements: BarChart,
    readonly replicationChangeSets: BarChart
  ) {}

  public static fromJSON(jsonObject: any): ReplicationStatusPage {
    if (!jsonObject) {
      return undefined;
    }
    return new ReplicationStatusPage(
      ActionTimestamp.fromJSON(jsonObject.timestamp),
      jsonObject.periodType,
      jsonObject.periodTitle,
      jsonObject.previous,
      jsonObject.next,
      BarChart2D.fromJSON(jsonObject.delay),
      BarChart.fromJSON(jsonObject.analysisDelay),
      BarChart.fromJSON(jsonObject.updateDelay),
      BarChart.fromJSON(jsonObject.replicationDelay),
      BarChart.fromJSON(jsonObject.replicationBytes),
      BarChart.fromJSON(jsonObject.replicationElements),
      BarChart.fromJSON(jsonObject.replicationChangeSets)
    );
  }
}
