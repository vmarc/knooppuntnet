import { LegBuildParams } from '@api/common/planner';
import { LegEnd } from '@api/common/planner';
import { NetworkType } from '@api/custom';
import { AppService } from '@app/app.service';
import { List } from 'immutable';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PlanLegData } from './plan-leg-data';
import { PlannerLegRepository } from './planner-leg-repository';

export class PlannerLegRepositoryImpl implements PlannerLegRepository {
  constructor(private appService: AppService) {}

  planLeg(
    networkType: NetworkType,
    source: LegEnd,
    sink: LegEnd,
    proposed: boolean
  ): Observable<PlanLegData> {
    const params: LegBuildParams = {
      networkType,
      source,
      sink,
      proposed,
    };
    return this.appService.leg(params).pipe(
      map((response) => {
        if (response.result) {
          return new PlanLegData(
            response.result.source,
            response.result.sink,
            List(response.result.routes)
          );
        }
        throw new Error('leg-not-found');
      })
    );
  }
}
