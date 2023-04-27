// this file is generated, please do not modify

import { Country } from '@api/custom';
import { Fact } from '@api/custom';
import { NetworkScope } from '@api/custom';
import { NetworkType } from '@api/custom';
import { RawNode } from '../data/raw';
import { RawRelation } from '../data/raw';
import { RawWay } from '../data/raw';

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
