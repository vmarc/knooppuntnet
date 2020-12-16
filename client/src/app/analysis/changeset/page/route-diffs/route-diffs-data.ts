import {List} from 'immutable';
import {KnownElements} from '@api/common/common/known-elements';
import {Ref} from '@api/common/common/ref';
import {RefDiffs} from '@api/common/diff/ref-diffs';
import {RouteChangeInfo} from '@api/common/route/route-change-info';

export class RouteDiffsData {

  constructor(readonly refDiffs: RefDiffs,
              readonly changeSetId: number,
              readonly knownElements: KnownElements,
              readonly routeChangeInfos: List<RouteChangeInfo>) {
  }

  findRouteChangeInfo(ref: Ref) {
    return this.routeChangeInfos.find(r => r.id === ref.id);
  }

}
