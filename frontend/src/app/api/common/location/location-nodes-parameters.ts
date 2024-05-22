// this file is generated, please do not modify

import { Fact } from '@api/custom';
import { BooleanParameter } from './boolean-parameter';
import { LastUpdatedParameter } from './last-updated-parameter';
import { SurveyParameter } from './survey-parameter';

export interface LocationNodesParameters {
  readonly fact: Fact;
  readonly survey: SurveyParameter;
  readonly lastUpdated: LastUpdatedParameter;
  readonly proposed: BooleanParameter;
  readonly pageSize: number;
  readonly pageIndex: number;
}
