import { Country } from '@api/common';
import { RouteType } from '@api/common';

export interface LocationKey {
  readonly routeType: RouteType;
  readonly country: Country;
  readonly name: string;
}
