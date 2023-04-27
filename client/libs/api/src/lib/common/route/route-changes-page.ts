// this file is generated, please do not modify

import { ChangesFilterOption } from '@api/common/changes/filter';
import { RouteChangeInfo } from './route-change-info';
import { RouteNameInfo } from './route-name-info';

export interface RouteChangesPage {
  readonly routeNameInfo: RouteNameInfo;
  readonly filterOptions: ChangesFilterOption[];
  readonly changes: RouteChangeInfo[];
  readonly totalCount: number;
  readonly changeCount: number;
}
