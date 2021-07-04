// this class is generated, please do not modify

import { List } from 'immutable';
import { LocationNodeInfo } from './location-node-info';
import { LocationSummary } from './location-summary';
import { TimeInfo } from '../time-info';

export class LocationNodesPage {
  constructor(
    readonly timeInfo: TimeInfo,
    readonly summary: LocationSummary,
    readonly nodes: List<LocationNodeInfo>
  ) {}

  public static fromJSON(jsonObject: any): LocationNodesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationNodesPage(
      TimeInfo.fromJSON(jsonObject.timeInfo),
      LocationSummary.fromJSON(jsonObject.summary),
      jsonObject.nodes
        ? List(
            jsonObject.nodes.map((json: any) => LocationNodeInfo.fromJSON(json))
          )
        : List()
    );
  }
}
