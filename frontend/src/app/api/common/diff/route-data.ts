// this file is generated, please do not modify

import { Country } from '@api/common';
import { Fact } from '@api/common';
import { NetworkType } from '@api/common';
import { RouteLocationAnalysis } from '@api/common';
import { MetaData } from '@api/common/data';
import { Way } from '@api/common/data';
import { RouteNode } from '@api/common/route';
import { Tag } from '@api/custom';

export interface RouteData {
  readonly relationId: number;
  readonly meta: MetaData;
  readonly countries: Country[];
  readonly networkTypes: NetworkType[];
  readonly name: string;
  readonly networkNodes: RouteNode[];
  readonly ways: Way[];
  readonly facts: Fact[];
  readonly meters: number;
  readonly locationAnalysis: RouteLocationAnalysis;
  readonly tiles: string[];
  readonly tags: Tag[];
}
