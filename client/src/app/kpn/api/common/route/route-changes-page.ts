// this file is generated, please do not modify

import { ChangesFilterOption } from '../changes/filter/changes-filter-option';
import { RouteChangeInfo } from './route-change-info';
import { RouteNameInfo } from './route-name-info';

export interface RouteChangesPage {
  readonly routeNameInfo: RouteNameInfo;
  readonly filterOptions: ChangesFilterOption[];
  readonly changes: RouteChangeInfo[];
  readonly totalCount: number;
  readonly changeCount: number;
}
