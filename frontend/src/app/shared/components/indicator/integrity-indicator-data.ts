import { RouteScope } from '@api/common';
import { RouteType } from '@api/common';

export class IntegrityIndicatorData {
  constructor(
    readonly routeType: RouteType,
    readonly routeScope: RouteScope,
    readonly actual: number,
    readonly expected: string
  ) {}

  color() {
    let color;
    if (this.expected !== '-') {
      if (+this.expected !== this.actual) {
        color = 'red';
      } else {
        color = 'green';
      }
    } else {
      color = 'gray';
    }
    return color;
  }
}
