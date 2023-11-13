// this file is generated, please do not modify

import { Ref } from '@api/common/common';
import { Check } from './check';

export interface NetworkFact {
  readonly name: string;
  readonly elementType: string;
  readonly elementIds: number[] | undefined;
  readonly elements: Ref[] | undefined;
  readonly checks: Check[] | undefined;
}
