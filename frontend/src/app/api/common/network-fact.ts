// this file is generated, please do not modify

import { Ref } from '@api/common/common/ref';
import { Check } from './check';
import { Fact } from './fact';

export interface NetworkFact {
  readonly fact: Fact;
  readonly elementType?: string;
  readonly elementIds?: number[];
  readonly elements?: Ref[];
  readonly checks?: Check[];
}
