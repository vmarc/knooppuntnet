// this file is generated, please do not modify

import { Country } from './country';
import { RouteScope } from './route-scope';
import { RouteType } from './route-type';

export interface RouteSummary {
  readonly countries: Country[];
  readonly nodeNetwork: boolean;
  readonly routeTypes: RouteType[];
  readonly scopes: RouteScope[];
  readonly name: string;
  readonly meters: number;
  readonly wayCount: number;
}
