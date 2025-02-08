import { LegBuildParams } from '@api/common/planner/leg-build-params';
import { LegEnd } from '@api/common/planner/leg-end';
import { RouteType } from '@api/common/route-type';
import { ApiService } from '@app/services';
import { List } from 'immutable';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PlanLegData } from './plan-leg-data';
import { PlannerLegRepository } from './planner-leg-repository';

export class PlannerLegRepositoryImpl implements PlannerLegRepository {
  constructor(private apiService: ApiService) {}

  planLeg(
    routeType: RouteType,
    source: LegEnd,
    sink: LegEnd,
    proposed: boolean
  ): Observable<PlanLegData> {
    const params: LegBuildParams = {
      routeType,
      source,
      sink,
      proposed,
    };
    return this.apiService.leg(params).pipe(
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
