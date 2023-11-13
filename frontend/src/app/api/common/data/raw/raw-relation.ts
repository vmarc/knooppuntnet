// this file is generated, please do not modify

import { Tags } from '@api/custom';
import { Timestamp } from '@api/custom';
import { RawMember } from './raw-member';

export interface RawRelation {
  readonly id: number;
  readonly version: number;
  readonly timestamp: Timestamp;
  readonly changeSetId: number;
  readonly members: RawMember[];
  readonly tags: Tags;
}
