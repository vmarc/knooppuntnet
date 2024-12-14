import { Fact } from '@api/common';
import { Subset } from '@api/custom';

export class SubsetFact {
  constructor(
    readonly subset: Subset,
    readonly fact: Fact
  ) {}
}
