// this file is generated, please do not modify

import { Fact } from '@api/common/fact';

export interface FactDiffs {
  readonly resolved: ReadonlyArray<Fact>;
  readonly introduced: ReadonlyArray<Fact>;
  readonly remaining: ReadonlyArray<Fact>;
}
