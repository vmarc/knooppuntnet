import {Ref} from "../../../../kpn/api/common/common/ref";
import {RouteChangeInfo} from "../../../../kpn/api/common/route/route-change-info";

export class RefRouteChangeInfo {
  constructor(public ref: Ref,
              public routeChangeInfo: RouteChangeInfo) {
  }
}
