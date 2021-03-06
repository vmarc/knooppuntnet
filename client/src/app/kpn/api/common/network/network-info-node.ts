// this file is generated, please do not modify

import { Day } from '../../custom/day';
import { Fact } from '../../custom/fact';
import { NodeIntegrityCheck } from '../node-integrity-check';
import { Ref } from '../common/ref';
import { Tags } from '../../custom/tags';
import { Timestamp } from '../../custom/timestamp';

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
  readonly timestamp: Timestamp;
  readonly lastSurvey: Day;
  readonly routeReferences: Ref[];
  readonly integrityCheck: NodeIntegrityCheck;
  readonly facts: Fact[];
  readonly tags: Tags;
}
