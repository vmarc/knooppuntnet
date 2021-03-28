import {PlannerContext} from '../context/planner-context';
import {FeatureId} from '../features/feature-id';
import {Plan} from './plan';
import {PlanFlag} from './plan-flag';
import {PlanUtil} from './plan-util';
import {PlanLeg} from './plan-leg';
import {List} from 'immutable';
import {LegEnd} from '@api/common/planner/leg-end';
import {PlanFlagType} from './plan-flag-type';
import {Observable, of} from 'rxjs';
import {map, switchMap} from 'rxjs/operators';

export class PlanReverser {

  constructor(private readonly context: PlannerContext) {
  }

  reverse(oldPlan: Plan): Observable<Plan> {

    if (oldPlan.legs.isEmpty()) {
      return of(oldPlan);
    }

    return this.buildLegs(oldPlan.legs.reverse(), List()).pipe(
      map(newLegs => {
        const sourceNode = newLegs.get(0).sourceNode;
        const sourceFlag = PlanUtil.startFlag(sourceNode.coordinate);
        return new Plan(sourceNode, sourceFlag, newLegs);
      })
    );
  }

  private buildLegs(oldLegs: List<PlanLeg>, result: List<PlanLeg>): Observable<List<PlanLeg>> {
    if (oldLegs.isEmpty()) {
      return of(result);
    }
    return this.buildLeg(oldLegs).pipe(
      switchMap(newLegs => this.buildLegs(oldLegs.shift(), result.concat(newLegs)))
    );
  }

  private buildLeg(oldLegs: List<PlanLeg>): Observable<List<PlanLeg>> {

    const oldLeg = oldLegs.get(0);
    const source = PlanUtil.legEndNode(+oldLeg.sinkNode.nodeId);

    let sink: LegEnd;
    if (oldLeg.sink.route) {
      sink = oldLeg.sink;
    } else {
      sink = PlanUtil.legEndNode(+oldLeg.sourceNode.nodeId);
    }

    const firstLeg$ = this.buildFirstLeg(source, sink, oldLeg.viaFlag);
    return firstLeg$.pipe(
      switchMap(firstLeg => {

        let sinkFlagType = PlanFlagType.via;
        if (oldLegs.size === 1) {
          sinkFlagType = PlanFlagType.end;
        } else if (oldLeg.viaFlag) {
          sinkFlagType = PlanFlagType.invisible;
        }

        if (!oldLeg.viaFlag) {
          const updatedFirstLeg = firstLeg.withSinkFlag(firstLeg.sinkFlag.to(sinkFlagType));
          return of(List([updatedFirstLeg]));
        }

        if (firstLeg.sinkNode.nodeId === oldLeg.sourceNode.nodeId) {
          const updatedFirstLeg = firstLeg.withSinkFlag(firstLeg.sinkFlag.to(sinkFlagType));
          return of(List([updatedFirstLeg]));
        }

        let firstLegSinkFlagType = PlanFlagType.invisible;
        if (firstLeg.sinkNode.nodeId === oldLeg.sourceNode.nodeId) {
          // this is the last leg, we have reached the end of the plan
          firstLegSinkFlagType = PlanFlagType.end;
        }

        const updatedFirstLeg = firstLeg.withSinkFlag(firstLeg.sinkFlag.to(firstLegSinkFlagType));

        return this.buildExtraLeg(firstLeg.sinkNode.nodeId, oldLeg.sourceNode.nodeId, sinkFlagType).pipe(
          map(extraLeg => List([updatedFirstLeg, extraLeg]))
        );
      })
    );
  }

  private buildFirstLeg(source: LegEnd, sink: LegEnd, viaFlag: PlanFlag): Observable<PlanLeg> {
    return this.context.fetchLeg(source, sink).pipe(
      map(data => {
        const sinkFlag = PlanUtil.viaFlag(data.sinkNode.coordinate);
        return PlanUtil.leg(data, sinkFlag, viaFlag);
      })
    );
  }

  private buildExtraLeg(sourceNodeId: string, sinkNodeId: string, sinkFlagType: PlanFlagType): Observable<PlanLeg> {
    const source = PlanUtil.legEndNode(+sourceNodeId);
    const sink = PlanUtil.legEndNode(+sinkNodeId);
    return this.context.fetchLeg(source, sink).pipe(
      map(data => {
        const sinkFlag = new PlanFlag(sinkFlagType, FeatureId.next(), data.sinkNode.coordinate);
        return PlanUtil.leg(data, sinkFlag, null);
      })
    );
  }

}
