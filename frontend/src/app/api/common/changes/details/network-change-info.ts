// this file is generated, please do not modify

import { ChangeType } from '@api/common/change-type';
import { Country } from '@api/common/country';
import { RouteType } from '@api/common/route-type';
import { MetaData } from '@api/common/data/meta-data';
import { IdDiffs } from '@api/common/diff/id-diffs';
import { RefDiffs } from '@api/common/diff/ref-diffs';
import { ChangeKey } from './change-key';

export interface NetworkChangeInfo {
  readonly rowIndex: number;
  readonly comment: string;
  readonly key: ChangeKey;
  readonly changeType: ChangeType;
  readonly country?: Country;
  readonly routeType: RouteType;
  readonly networkId: number;
  readonly networkName: string;
  readonly before?: MetaData;
  readonly after?: MetaData;
  readonly networkDataUpdated: boolean;
  readonly networkNodes: RefDiffs;
  readonly routes: RefDiffs;
  readonly nodes: IdDiffs;
  readonly ways: IdDiffs;
  readonly relations: IdDiffs;
  readonly happy: boolean;
  readonly investigate: boolean;
}
