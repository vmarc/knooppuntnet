// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { NetworkMapNode } from './network-map-node';
import { NetworkSummary } from './network-summary';

export interface NetworkMapPage {
  readonly summary: NetworkSummary;
  readonly nodes: ReadonlyArray<NetworkMapNode>;
  readonly networkNodeIds: ReadonlyArray<number>;
  readonly connectionNodeIds: ReadonlyArray<number>;
  readonly networkRouteIds: ReadonlyArray<number>;
  readonly connectionRouteIds: ReadonlyArray<number>;
  readonly bounds: Bounds;
}
