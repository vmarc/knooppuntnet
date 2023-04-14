// this file is generated, please do not modify

import { OrphanRouteInfo } from '@api/common';
import { TimeInfo } from '@api/common';
import { SubsetInfo } from './subset-info';

export interface SubsetOrphanRoutesPage {
  readonly timeInfo: TimeInfo;
  readonly subsetInfo: SubsetInfo;
  readonly routes: OrphanRouteInfo[];
}
