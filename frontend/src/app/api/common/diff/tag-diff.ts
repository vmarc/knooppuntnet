// this file is generated, please do not modify

import { TagDiffType } from './tag-diff-type';

export interface TagDiff {
  readonly action: TagDiffType;
  readonly key: string;
  readonly valueBefore?: string;
  readonly valueAfter?: string;
}
