import { LegEnd } from '@api/common/planner';
import { NetworkType } from '@api/custom';
import { Map } from 'immutable';
import { List } from 'immutable';
import { Observable, of } from 'rxjs';
import { PlanUtil } from '../plan/plan-util';
import { PlanLegData } from './plan-leg-data';
import { PlannerLegRepository } from './planner-leg-repository';

export class PlannerLegRepositoryMock implements PlannerLegRepository {
  private planLegDetails: Map<string, PlanLegData> = Map<string, PlanLegData>();

  planLeg(networkType: NetworkType, source: LegEnd, sink: LegEnd): Observable<PlanLegData> {
    const key = PlanUtil.key(source, sink);
    const data = this.planLegDetails.get(key);
    if (!data) {
      let message = `PlannerLegRepositoryMock: could not find leg with key ${key}`;
      if (this.planLegDetails.isEmpty()) {
        message += ', mock data does not contain any legs.';
      } else {
        message += ', mock data contain following legs: ';
        message += List(this.planLegDetails.keys()).join(', ');
      }
      throw new Error(message);
    }
    return of(data);
  }

  add(planLegData: PlanLegData): void {
    const key = PlanUtil.key(planLegData.source, planLegData.sink);
    this.planLegDetails = this.planLegDetails.set(key, planLegData);
  }
}
