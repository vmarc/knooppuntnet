// this file is generated, please do not modify

import { NodeIntegrityCheck } from '@api/common';
import { Ref } from '@api/common/common';
import { Day } from '@api/custom';
import { Fact } from '@api/custom';
import { Tags } from '@api/custom';
import { Timestamp } from '@api/custom';

export interface NetworkInfoNode {
  readonly id: number;
  readonly name: string;
  readonly longName: string;
  readonly latitude: string;
  readonly longitude: string;
  readonly connection: boolean;
  readonly roleConnection: boolean;
  readonly definedInRelation: boolean;
  readonly definedInRoute: boolean;
  readonly proposed: boolean;
  readonly timestamp: Timestamp;
  readonly lastSurvey: Day;
  readonly routeReferences: Ref[];
  readonly integrityCheck: NodeIntegrityCheck;
  readonly facts: Fact[];
  readonly tags: Tags;
}
