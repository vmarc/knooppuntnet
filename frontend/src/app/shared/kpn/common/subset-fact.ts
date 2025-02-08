import { Fact } from '@api/common/fact';
import { Subset } from '@api/custom/subset';

export class SubsetFact {
  constructor(
    readonly subset: Subset,
    readonly fact: Fact
  ) {}
}
