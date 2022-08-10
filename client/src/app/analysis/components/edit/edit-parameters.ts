import { Bounds } from '@api/common/bounds';

export interface EditParameters {
  readonly bounds?: Bounds;
  readonly nodeIds?: number[];
  readonly wayIds?: number[];
  readonly relationIds?: number[];
  readonly fullRelation?: boolean;
}
