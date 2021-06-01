// this file is generated, please do not modify

import { Tags } from '../../../custom/tags';
import { Timestamp } from '../../../custom/timestamp';

export interface RawWay {
  readonly id: number;
  readonly version: number;
  readonly timestamp: Timestamp;
  readonly changeSetId: number;
  readonly nodeIds: number[];
  readonly tags: Tags;
}
