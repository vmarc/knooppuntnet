import { Bounds } from '@api/common/bounds';

export interface EditParameters {
  readonly bounds?: Bounds;
  readonly nodeIds?: ReadonlyArray<number>;
  readonly wayIds?: ReadonlyArray<number>;
  readonly relationIds?: ReadonlyArray<number>;
  readonly fullRelation?: boolean;
}
