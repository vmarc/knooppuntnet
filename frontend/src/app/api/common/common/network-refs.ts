// this file is generated, please do not modify

import { Country } from '@api/common/country';
import { RouteType } from '@api/common/route-type';
import { Ref } from './ref';

export interface NetworkRefs {
  readonly country: Country;
  readonly routeType: RouteType;
  readonly networkRef?: Ref;
  readonly refType: string;
  readonly refs: Ref[];
}
