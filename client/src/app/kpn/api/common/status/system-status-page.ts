// this class is generated, please do not modify

import {ActionTimestamp} from './action-timestamp';
import {BarChart} from './bar-chart';

export class SystemStatusPage {

  constructor(readonly timestamp: ActionTimestamp,
              readonly periodType: string,
              readonly periodTitle: string,
              readonly previous: string,
              readonly next: string,
              readonly backendDiskSpaceUsed: BarChart,
              readonly backendDiskSpaceAvailable: BarChart,
              readonly backendDiskSpaceOverpass: BarChart,
              readonly analysisDocCount: BarChart,
              readonly analysisDiskSize: BarChart,
              readonly analysisDiskSizeExternal: BarChart,
              readonly analysisDataSize: BarChart,
              readonly changesDocCount: BarChart,
              readonly changesDiskSize: BarChart,
              readonly changesDiskSizeExternal: BarChart,
              readonly changesDataSize: BarChart) {
  }

  public static fromJSON(jsonObject: any): SystemStatusPage {
    if (!jsonObject) {
      return undefined;
    }
    return new SystemStatusPage(
      ActionTimestamp.fromJSON(jsonObject.timestamp),
      jsonObject.periodType,
      jsonObject.periodTitle,
      jsonObject.previous,
      jsonObject.next,
      BarChart.fromJSON(jsonObject.backendDiskSpaceUsed),
      BarChart.fromJSON(jsonObject.backendDiskSpaceAvailable),
      BarChart.fromJSON(jsonObject.backendDiskSpaceOverpass),
      BarChart.fromJSON(jsonObject.analysisDocCount),
      BarChart.fromJSON(jsonObject.analysisDiskSize),
      BarChart.fromJSON(jsonObject.analysisDiskSizeExternal),
      BarChart.fromJSON(jsonObject.analysisDataSize),
      BarChart.fromJSON(jsonObject.changesDocCount),
      BarChart.fromJSON(jsonObject.changesDiskSize),
      BarChart.fromJSON(jsonObject.changesDiskSizeExternal),
      BarChart.fromJSON(jsonObject.changesDataSize)
    );
  }
}
