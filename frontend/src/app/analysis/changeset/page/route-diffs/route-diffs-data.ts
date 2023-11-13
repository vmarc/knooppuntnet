import { KnownElements } from '@api/common/common';
import { Ref } from '@api/common/common';
import { RefDiffs } from '@api/common/diff';
import { RouteChangeInfo } from '@api/common/route';
import { List } from 'immutable';

export class RouteDiffsData {
  constructor(
    readonly refDiffs: RefDiffs,
    readonly changeSetId: number,
    readonly knownElements: KnownElements,
    readonly routeChangeInfos: List<RouteChangeInfo>
  ) {}

  findRouteChangeInfo(ref: Ref) {
    return this.routeChangeInfos.find((r) => r.id === ref.id);
  }
}
