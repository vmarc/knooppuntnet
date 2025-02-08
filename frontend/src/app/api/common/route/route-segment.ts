// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';

export interface RouteSegment {
  readonly id: number;
  readonly startNodeId: number;
  readonly endNodeId: number;
  readonly meters: number;
  readonly bounds: Bounds;
  readonly elementIds: number[];
}
