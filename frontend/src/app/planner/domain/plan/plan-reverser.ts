import { LegEnd } from '@api/common/planner/leg-end';
import { Observable, of } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { PlannerContext } from '../context/planner-context';
import { FeatureId } from '../features/feature-id';
import { Plan } from './plan';
import { PlanFlag } from './plan-flag';
import { PlanFlagType } from './plan-flag-type';
import { PlanLeg } from './plan-leg';
import { PlanUtil } from './plan-util';

export class PlanReverser {
  constructor(private readonly context: PlannerContext) {}

  reverse(oldPlan: Plan): Observable<Plan> {
    if (oldPlan.legs.length === 0) {
      return of(oldPlan);
    }

    return this.buildLegs([...oldPlan.legs].reverse(), []).pipe(
      map((newLegs) => {
        const sourceNode = newLegs[0].sourceNode;
        const sourceFlag = PlanUtil.startFlag(sourceNode.coordinate);
        return new Plan(sourceNode, sourceFlag, newLegs);
      })
    );
  }

  private buildLegs(
    oldLegs: ReadonlyArray<PlanLeg>,
    result: ReadonlyArray<PlanLeg>
  ): Observable<ReadonlyArray<PlanLeg>> {
    if (oldLegs.length === 0) {
      return of(result);
    }
    return this.buildLeg(oldLegs).pipe(
      switchMap((newLegs) => this.buildLegs(oldLegs.slice(1), result.concat(newLegs)))
    );
  }

  private buildLeg(oldLegs: ReadonlyArray<PlanLeg>): Observable<ReadonlyArray<PlanLeg>> {
    const oldLeg = oldLegs[0];
    const source = PlanUtil.legEndNode(+oldLeg.sinkNode.nodeId);

    let sink: LegEnd;
    if (oldLeg.sink.route) {
      sink = oldLeg.sink;
    } else {
      sink = PlanUtil.legEndNode(+oldLeg.sourceNode.nodeId);
    }

    const firstLeg$ = this.buildFirstLeg(source, sink, oldLeg.viaFlag);
    return firstLeg$.pipe(
      switchMap((firstLeg) => {
        let sinkFlagType = PlanFlagType.via;
        if (oldLegs.length === 1) {
          sinkFlagType = PlanFlagType.end;
        } else if (oldLeg.viaFlag) {
          sinkFlagType = PlanFlagType.invisible;
        }

        if (!oldLeg.viaFlag) {
          const updated = firstLeg.withSinkFlag(firstLeg.sinkFlag.to(sinkFlagType));
          return of([updated]);
        }

        if (firstLeg.sinkNode.nodeId === oldLeg.sourceNode.nodeId) {
          const updated = firstLeg.withSinkFlag(firstLeg.sinkFlag.to(sinkFlagType));
          return of([updated]);
        }

        let firstLegSinkFlagType = PlanFlagType.invisible;
        if (firstLeg.sinkNode.nodeId === oldLeg.sourceNode.nodeId) {
          // this is the last leg, we have reached the end of the plan
          firstLegSinkFlagType = PlanFlagType.end;
        }

        const updatedFirstLeg = firstLeg.withSinkFlag(firstLeg.sinkFlag.to(firstLegSinkFlagType));

        return this.buildExtraLeg(
          firstLeg.sinkNode.nodeId,
          oldLeg.sourceNode.nodeId,
          sinkFlagType
        ).pipe(map((extraLeg) => [updatedFirstLeg, extraLeg]));
      })
    );
  }

  private buildFirstLeg(source: LegEnd, sink: LegEnd, viaFlag: PlanFlag): Observable<PlanLeg> {
    return this.context.fetchLeg(source, sink).pipe(
      map((data) => {
        const sinkFlag = PlanUtil.viaFlag(data.sinkNode.coordinate);
        return PlanUtil.leg(data, sinkFlag, viaFlag);
      })
    );
  }

  private buildExtraLeg(
    sourceNodeId: string,
    sinkNodeId: string,
    sinkFlagType: PlanFlagType
  ): Observable<PlanLeg> {
    const source = PlanUtil.legEndNode(+sourceNodeId);
    const sink = PlanUtil.legEndNode(+sinkNodeId);
    return this.context.fetchLeg(source, sink).pipe(
      map((data) => {
        const sinkFlag = new PlanFlag(sinkFlagType, FeatureId.next(), data.sinkNode.coordinate);
        return PlanUtil.leg(data, sinkFlag, null);
      })
    );
  }
}
