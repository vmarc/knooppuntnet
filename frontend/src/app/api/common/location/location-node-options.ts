// this file is generated, please do not modify

import { ServerFilterGroup } from '@api/common/changes/filter/server-filter-group';

export interface LocationNodeOptions {
  readonly integrityCheck: ServerFilterGroup;
  readonly integrityCheckFailed: ServerFilterGroup;
  readonly fact: ServerFilterGroup;
  readonly survey: ServerFilterGroup;
  readonly lastUpdated: ServerFilterGroup;
  readonly proposed: ServerFilterGroup;
  readonly referencedInRoutes: ServerFilterGroup;
  readonly totalNodeCount: number;
}
