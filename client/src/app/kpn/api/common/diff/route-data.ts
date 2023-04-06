// this file is generated, please do not modify

import { Country } from '@api/custom/country';
import { Fact } from '@api/custom/fact';
import { NetworkScope } from '@api/custom/network-scope';
import { NetworkType } from '@api/custom/network-type';
import { RawNode } from '../data/raw/raw-node';
import { RawRelation } from '../data/raw/raw-relation';
import { RawWay } from '../data/raw/raw-way';

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
