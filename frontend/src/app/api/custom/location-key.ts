import { Country } from '@api/common/country';
import { RouteType } from '@api/common/route-type';

export interface LocationKey {
  readonly routeType: RouteType;
  readonly country: Country;
  readonly name: string;
}
