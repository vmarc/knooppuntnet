// this file is generated, please do not modify

import { ChangesFilter } from '../changes/filter/changes-filter';
import { RouteChangeInfo } from './route-change-info';
import { RouteInfo } from './route-info';

export interface RouteChangesPage {
  readonly route: RouteInfo;
  readonly filter: ChangesFilter;
  readonly changes: RouteChangeInfo[];
  readonly incompleteWarning: boolean;
  readonly totalCount: number;
  readonly changeCount: number;
}
