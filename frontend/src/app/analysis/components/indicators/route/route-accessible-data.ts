import { RouteType } from '@api/common';

export class RouteAccessibleData {
  constructor(
    readonly routeType: RouteType,
    readonly accessible: boolean,
    readonly color: string
  ) {}
}
