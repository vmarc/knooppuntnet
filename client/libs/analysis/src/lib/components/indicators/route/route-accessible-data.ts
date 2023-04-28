import { NetworkType } from '@api/custom';

export class RouteAccessibleData {
  constructor(
    readonly networkType: NetworkType,
    readonly accessible: boolean,
    readonly color: string
  ) {}
}
