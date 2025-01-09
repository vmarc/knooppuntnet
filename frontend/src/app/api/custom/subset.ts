import { Country } from '@api/common';
import { RouteType } from '@api/common';

export interface Subset {
  readonly country: Country;
  readonly routeType: RouteType;
}
