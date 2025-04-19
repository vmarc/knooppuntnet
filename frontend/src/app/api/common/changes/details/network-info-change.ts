// this file is generated, please do not modify

import { ChangeType } from '@api/common/change-type';
import { Country } from '@api/common/country';
import { IdDiffs } from '@api/common/diff/id-diffs';
import { NetworkDataUpdate } from '@api/common/diff/network-data-update';
import { RefDiffs } from '@api/common/diff/ref-diffs';
import { RouteType } from '@api/common/route-type';
import { ChangeKey } from './change-key';

export interface NetworkInfoChange {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly changeType: ChangeType;
  readonly country?: Country;
  readonly routeType: RouteType;
  readonly networkId: number;
  readonly networkName: string;
  readonly networkDataUpdate?: NetworkDataUpdate;
  readonly nodeDiffs: RefDiffs;
  readonly routeDiffs: RefDiffs;
  readonly extraNodeDiffs: IdDiffs;
  readonly extraWayDiffs: IdDiffs;
  readonly extraRelationDiffs: IdDiffs;
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly impact: boolean;
}
