import { Ref } from '@api/common/common/ref';
import { Fact } from '@api/common/fact';

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
