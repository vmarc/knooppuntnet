import { RouteType } from '@api/common/route-type';

export class RouteAccessibleData {
  constructor(
    readonly routeType: RouteType,
    readonly accessible: boolean,
    readonly color: string
  ) {}
}
