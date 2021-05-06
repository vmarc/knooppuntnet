// this class is generated, please do not modify

import { OrphanRouteInfo } from '../orphan-route-info';
import { TimeInfo } from '../time-info';
import { SubsetInfo } from './subset-info';

export class SubsetOrphanRoutesPage {
  constructor(
    readonly timeInfo: TimeInfo,
    readonly subsetInfo: SubsetInfo,
    readonly routes: Array<OrphanRouteInfo>
  ) {}

  static fromJSON(jsonObject: any): SubsetOrphanRoutesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new SubsetOrphanRoutesPage(
      TimeInfo.fromJSON(jsonObject.timeInfo),
      SubsetInfo.fromJSON(jsonObject.subsetInfo),
      jsonObject.routes.map((json: any) => OrphanRouteInfo.fromJSON(json))
    );
  }
}
