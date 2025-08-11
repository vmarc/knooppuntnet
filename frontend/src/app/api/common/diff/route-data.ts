// this file is generated, please do not modify

import { Country } from '@api/common/country';
import { Fact } from '@api/common/fact';
import { RouteLocationAnalysis } from '@api/common/route-location-analysis';
import { RouteType } from '@api/common/route-type';
import { MetaData } from '@api/common/data/meta-data';
import { RouteNode } from '@api/common/route/route-node';
import { Tag } from '@api/custom/tag';

export interface RouteData {
  readonly relationId: number;
  readonly meta: MetaData;
  readonly countries: Country[];
  readonly routeTypes: RouteType[];
  readonly name: string;
  readonly networkNodes: RouteNode[];
  readonly facts: Fact[];
  readonly meters: number;
  readonly locationAnalysis: RouteLocationAnalysis;
  readonly tags: Tag[];
}
