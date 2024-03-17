import { Ref } from '@api/common/common';
import { RouteChangeInfo } from '@api/common/route';

export class RefRouteChangeInfo {
  constructor(
    public ref: Ref,
    public routeChangeInfo: RouteChangeInfo
  ) {}
}
