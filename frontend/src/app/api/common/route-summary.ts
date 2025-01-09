// this file is generated, please do not modify

import { Tag } from '@api/custom';
import { Timestamp } from '@api/custom';
import { Country } from './country';
import { RouteType } from './route-type';
import { RouteScope } from './route-scope';

export interface RouteSummary {
  readonly id: number;
  readonly countries: Country[];
  readonly nodeNetwork: boolean;
  readonly routeTypes: RouteType[];
  readonly scopes: RouteScope[];
  readonly name: string;
  readonly meters: number;
  readonly broken: boolean;
  readonly inaccessible: boolean;
  readonly wayCount: number;
  readonly timestamp: Timestamp;
  readonly tags: Tag[];
}
