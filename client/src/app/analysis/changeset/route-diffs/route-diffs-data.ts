import {List} from "immutable";
import {KnownElements} from "../../../kpn/api/common/common/known-elements";
import {Ref} from "../../../kpn/api/common/common/ref";
import {RefDiffs} from "../../../kpn/api/common/diff/ref-diffs";
import {RouteChangeInfo} from "../../../kpn/api/common/route/route-change-info";

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
