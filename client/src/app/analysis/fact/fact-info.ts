import { Fact } from '@api/custom/fact';
import { Ref } from '@api/common/common/ref';

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
