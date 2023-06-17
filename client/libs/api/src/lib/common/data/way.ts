// this file is generated, please do not modify

import { RawWay } from '@api/common/data/raw';
import { Node } from './node';

export interface Way {
  readonly raw: RawWay;
  readonly nodes: Node[];
  readonly length: number;
}
