import {List} from "immutable";
import {KnownElements} from "../../../kpn/shared/common/known-elements";
import {Ref} from "../../../kpn/shared/common/ref";
import {RefDiffs} from "../../../kpn/shared/diff/ref-diffs";
import {RouteChangeInfo} from "../../../kpn/shared/route/route-change-info";

export class RouteDiffsData {

  constructor(readonly refDiffs: RefDiffs,
              readonly changeSetId: number,
              readonly knownElements: KnownElements,
              readonly routeChangeInfos: List<RouteChangeInfo>) {
  }

  findRouteChangeInfo(ref: Ref) {
    return this.routeChangeInfos.filter(r => r.id === ref.id);
  }

}
