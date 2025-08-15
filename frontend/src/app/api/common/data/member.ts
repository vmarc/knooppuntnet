// this file is generated, please do not modify

import { Relation } from '@api/common/relation';
import { Node } from './node';
import { Way } from './way';

export interface Member {
  readonly node?: Node;
  readonly way?: Way;
  readonly relation?: Relation;
  readonly relationId?: number;
  readonly role?: string;
}
