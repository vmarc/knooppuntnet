import { Country } from '@api/common/country';
import { RouteType } from '@api/common/route-type';

export interface Subset {
  readonly country: Country;
  readonly routeType: RouteType;
}
