import { Ref } from '@api/common/common';
import { Fact } from '@api/custom';

export class FactInfo {
  constructor(
    public fact: Fact,
    public networkRef?: Ref,
    public routeRef?: Ref,
    public nodeRef?: Ref,
    public unexpectedNodeIds?: number[],
    public unexpectedRelationIds?: number[]
  ) {}
}
