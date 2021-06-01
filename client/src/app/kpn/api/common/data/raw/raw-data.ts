// this file is generated, please do not modify

import { RawNode } from './raw-node';
import { RawRelation } from './raw-relation';
import { RawWay } from './raw-way';
import { Timestamp } from '../../../custom/timestamp';

export interface RawData {
  readonly timestamp: Timestamp;
  readonly nodes: RawNode[];
  readonly ways: RawWay[];
  readonly relations: RawRelation[];
}
