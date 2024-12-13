import { NetworkScope } from '@api/custom';
import { NetworkType } from '@api/common';

export class IntegrityIndicatorData {
  constructor(
    readonly networkType: NetworkType,
    readonly networkScope: NetworkScope,
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
