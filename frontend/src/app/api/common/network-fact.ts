// this file is generated, please do not modify

import { Ref } from '@api/common/common';
import { Fact } from '@api/common/fact';
import { Check } from './check';

export interface NetworkFact {
  readonly fact: Fact;
  readonly elementType: string;
  readonly elementIds: number[] | undefined;
  readonly elements: Ref[] | undefined;
  readonly checks: Check[] | undefined;
}
