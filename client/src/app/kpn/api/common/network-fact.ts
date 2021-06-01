// this file is generated, please do not modify

import { Check } from './check';
import { Ref } from './common/ref';

export interface NetworkFact {
  readonly name: string;
  readonly elementType: string;
  readonly elementIds: number[] | undefined;
  readonly elements: Ref[] | undefined;
  readonly checks: Check[] | undefined;
}
