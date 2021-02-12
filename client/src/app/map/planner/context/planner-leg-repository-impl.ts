import {LegBuildParams} from '@api/common/planner/leg-build-params';
import {LegEnd} from '@api/common/planner/leg-end';
import {NetworkType} from '@api/custom/network-type';
import {List} from 'immutable';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {AppService} from '../../../app.service';
import {PlanLegData} from './plan-leg-data';
import {PlannerLegRepository} from './planner-leg-repository';

export class PlannerLegRepositoryImpl implements PlannerLegRepository {

  constructor(private appService: AppService) {
  }

  planLeg(networkType: NetworkType, source: LegEnd, sink: LegEnd): Observable<PlanLegData> {
    const params = new LegBuildParams(networkType.name, source, sink);
    return this.appService.leg(params).pipe(
      map(response => {
        if (response.result) {
          return new PlanLegData(response.result.source, response.result.sink, List(response.result.routes));
        }
        throw new Error('leg-not-found');
      })
    );
  }
}
