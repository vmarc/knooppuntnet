// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';

export interface MonitorRouteSummary {
  readonly adminUser: boolean;
  readonly groupName: string;
  readonly routeName: string;
  readonly routeDescription: string;
  readonly routeId: string;
  readonly relationId: number;
  readonly relationIds: number[];
  readonly memberCount: number;
  readonly segmentCount: number;
  readonly deviationCount: number;
  readonly bounds?: Bounds;
}
