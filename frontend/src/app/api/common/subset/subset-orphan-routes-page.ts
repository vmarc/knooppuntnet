// this file is generated, please do not modify

import { OrphanRouteInfo } from '@api/common/orphan-route-info';
import { TimeInfo } from '@api/common/time-info';
import { SubsetInfo } from './subset-info';

export interface SubsetOrphanRoutesPage {
  readonly timeInfo: TimeInfo;
  readonly subsetInfo: SubsetInfo;
  readonly routes: OrphanRouteInfo[];
}
