// this file is generated, please do not modify

import { Country } from '@api/common/country';
import { Fact } from '@api/common/fact';
import { RouteLocationAnalysis } from '@api/common/route-location-analysis';
import { RouteType } from '@api/common/route-type';
import { Raw } from '@api/common/data/raw/raw';
import { RouteNode } from '@api/common/route/route-node';

export interface RouteData {
  readonly relationId: number;
  readonly raw: Raw;
  readonly countries: Country[];
  readonly routeTypes: RouteType[];
  readonly name: string;
  readonly networkNodes: RouteNode[];
  readonly facts: Fact[];
  readonly meters: number;
  readonly locationAnalysis: RouteLocationAnalysis;
}
