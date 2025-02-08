// this file is generated, please do not modify

import { Fact } from '@api/common/fact';

export interface FactDiffs {
  readonly resolved: Fact[];
  readonly introduced: Fact[];
  readonly remaining: Fact[];
}
