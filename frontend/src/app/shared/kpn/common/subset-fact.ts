import { Subset } from '@api/custom';

export class SubsetFact {
  constructor(readonly subset: Subset, readonly factName: string) {}
}
