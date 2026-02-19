// this file is generated, please do not modify

import { Country } from '@api/common/country';
import { Fact } from '@api/common/fact';
import { RouteType } from '@api/common/route-type';
import { Raw } from '@api/common/data/raw/raw';
import { RouteNode } from '@api/common/route/route-node';

export interface RouteData {
  readonly relationId: number;
  readonly raw: Raw;
  readonly countries: ReadonlyArray<Country>;
  readonly routeTypes: ReadonlyArray<RouteType>;
  readonly name: string;
  readonly networkNodes: ReadonlyArray<RouteNode>;
  readonly facts: ReadonlyArray<Fact>;
  readonly meters: number;
}
