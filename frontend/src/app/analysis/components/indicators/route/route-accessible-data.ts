import { NetworkType } from '@api/common';

export class RouteAccessibleData {
  constructor(
    readonly networkType: NetworkType,
    readonly accessible: boolean,
    readonly color: string
  ) {}
}
