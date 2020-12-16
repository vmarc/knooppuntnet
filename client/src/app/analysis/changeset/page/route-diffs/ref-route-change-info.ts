import {Ref} from '@api/common/common/ref';
import {RouteChangeInfo} from '@api/common/route/route-change-info';

export class RefRouteChangeInfo {
  constructor(public ref: Ref,
              public routeChangeInfo: RouteChangeInfo) {
  }
}
