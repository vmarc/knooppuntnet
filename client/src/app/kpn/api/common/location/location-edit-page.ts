// this class is generated, please do not modify

import {Bounds} from '../bounds';
import {TimeInfo} from '../time-info';
import {LocationSummary} from './location-summary';

export class LocationEditPage {

  constructor(readonly timeInfo: TimeInfo,
              readonly summary: LocationSummary,
              readonly tooManyNodes: boolean,
              readonly maxNodes: number,
              readonly bounds: Bounds,
              readonly nodeIds: Array<number>,
              readonly routeIds: Array<number>) {
  }

  public static fromJSON(jsonObject: any): LocationEditPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationEditPage(
      TimeInfo.fromJSON(jsonObject.timeInfo),
      LocationSummary.fromJSON(jsonObject.summary),
      jsonObject.tooManyNodes,
      jsonObject.maxNodes,
      Bounds.fromJSON(jsonObject.bounds),
      jsonObject.nodeIds,
      jsonObject.routeIds
    );
  }
}
