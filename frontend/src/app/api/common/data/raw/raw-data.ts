// this file is generated, please do not modify

import { Timestamp } from '@api/custom/timestamp';
import { RawNode } from './raw-node';
import { RawRelation } from './raw-relation';
import { RawWay } from './raw-way';

export interface RawData {
  readonly timestamp?: Timestamp;
  readonly nodes: ReadonlyArray<RawNode>;
  readonly ways: ReadonlyArray<RawWay>;
  readonly relations: ReadonlyArray<RawRelation>;
}
