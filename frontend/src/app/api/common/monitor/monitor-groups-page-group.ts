// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';

export interface MonitorGroupsPageGroup {
  readonly id: string;
  readonly name: string;
  readonly description: string;
  readonly routeCount: number;
  readonly monitorRouteIds: ReadonlyArray<string>;
  readonly bounds?: Bounds;
}
