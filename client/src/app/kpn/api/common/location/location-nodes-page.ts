// this class is generated, please do not modify

import { TimeInfo } from '../time-info';
import { LocationNodeInfo } from './location-node-info';
import { LocationSummary } from './location-summary';

export class LocationNodesPage {
  constructor(
    readonly timeInfo: TimeInfo,
    readonly summary: LocationSummary,
    readonly nodeCount: number,
    readonly allNodeCount: number,
    readonly factsNodeCount: number,
    readonly surveyNodeCount: number,
    readonly nodes: Array<LocationNodeInfo>
  ) {}

  static fromJSON(jsonObject: any): LocationNodesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationNodesPage(
      TimeInfo.fromJSON(jsonObject.timeInfo),
      LocationSummary.fromJSON(jsonObject.summary),
      jsonObject.nodeCount,
      jsonObject.allNodeCount,
      jsonObject.factsNodeCount,
      jsonObject.surveyNodeCount,
      jsonObject.nodes?.map((json: any) => LocationNodeInfo.fromJSON(json))
    );
  }
}
