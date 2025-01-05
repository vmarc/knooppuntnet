// this file is generated, please do not modify

import { Fact } from '@api/common';
import { BooleanParameter } from './boolean-parameter';
import { LastUpdatedParameter } from './last-updated-parameter';
import { SurveyParameter } from './survey-parameter';

export interface LocationNodesParameters {
  readonly integrityCheck?: BooleanParameter;
  readonly integrityCheckFailed?: BooleanParameter;
  readonly fact?: Fact;
  readonly survey?: SurveyParameter;
  readonly lastUpdated?: LastUpdatedParameter;
  readonly proposed?: BooleanParameter;
  readonly referencedInRoutes?: BooleanParameter;
  readonly pageSize: number;
  readonly pageIndex: number;
}
