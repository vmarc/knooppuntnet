// this file is generated, please do not modify

import { RawMember } from './raw-member';
import { Tags } from '../../../custom/tags';
import { Timestamp } from '../../../custom/timestamp';

export interface RawRelation {
  readonly id: number;
  readonly version: number;
  readonly timestamp: Timestamp;
  readonly changeSetId: number;
  readonly members: RawMember[];
  readonly tags: Tags;
}
