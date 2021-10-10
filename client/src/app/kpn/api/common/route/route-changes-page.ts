// this file is generated, please do not modify

import { ChangesFilter } from '../changes/filter/changes-filter';
import { RouteChangeInfo } from './route-change-info';
import { RouteNameInfo } from './route-name-info';

export interface RouteChangesPage {
  readonly routeNameInfo: RouteNameInfo;
  readonly filter: ChangesFilter;
  readonly changes: RouteChangeInfo[];
  readonly totalCount: number;
  readonly changeCount: number;
}
