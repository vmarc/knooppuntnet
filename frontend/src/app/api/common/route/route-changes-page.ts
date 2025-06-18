// this file is generated, please do not modify

import { ChangesFilterOption } from '@api/common/changes/filter/changes-filter-option';
import { RouteChangeInfo } from './route-change-info';
import { RouteInfo } from './route-info';

export interface RouteChangesPage {
  readonly routeInfo: RouteInfo;
  readonly filterOptions: ChangesFilterOption[];
  readonly changes: RouteChangeInfo[];
}
