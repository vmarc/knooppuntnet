// this file is generated, please do not modify

import { RawNode } from '@api/common/data/raw';
import { RawRelation } from '@api/common/data/raw';
import { RawWay } from '@api/common/data/raw';
import { Country } from '@api/custom';
import { Fact } from '@api/custom';
import { NetworkScope } from '@api/custom';
import { NetworkType } from '@api/custom';

export interface RouteData {
  readonly country: Country;
  readonly networkType: NetworkType;
  readonly networkScope: NetworkScope;
  readonly relation: RawRelation;
  readonly name: string;
  readonly networkNodes: RawNode[];
  readonly nodes: RawNode[];
  readonly ways: RawWay[];
  readonly relations: RawRelation[];
  readonly facts: Fact[];
}
