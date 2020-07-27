import {PlannerContext} from "../context/planner-context";
import {FeatureId} from "../features/feature-id";
import {Plan} from "./plan";
import {PlanFlag} from "./plan-flag";
import {PlanUtil} from "./plan-util";
import {PlanLeg} from "./plan-leg";
import {List} from "immutable";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";
import {PlanFlagType} from "./plan-flag-type";
import {Observable, of} from "rxjs";
import {map, switchMap} from "rxjs/operators";

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
        const sourceFlag = PlanFlag.start(FeatureId.next(), sourceNode.coordinate);
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
    if (oldLeg.sink.route !== null) {
      sink = oldLeg.sink;
    } else {
      sink = PlanUtil.legEndNode(+oldLeg.sourceNode.nodeId);
    }

    const firstLeg$ = this.buildFirstLeg(source, sink, oldLeg.viaFlag);
    return firstLeg$.pipe(
      switchMap(firstLeg => {

        let sinkFlagType = PlanFlagType.Via;
        if (oldLegs.size === 1) {
          sinkFlagType = PlanFlagType.End;
        }

        if (firstLeg.sinkNode.nodeId === oldLeg.sourceNode.nodeId) {
          // already at target node, no extra leg needed
          let updatedFirstLeg = firstLeg;
          if (sinkFlagType === PlanFlagType.End) {
            updatedFirstLeg = updatedFirstLeg.withSinkFlag(updatedFirstLeg.sinkFlag.toEnd());
          }
          return of(List([updatedFirstLeg]));
        }

        return this.buildExtraLeg(firstLeg.sinkNode.nodeId, oldLeg.sourceNode.nodeId, sinkFlagType).pipe(
          map(extraLeg => {
            return List([firstLeg, extraLeg]);
          })
        );
      })
    );
  }

  private buildFirstLeg(source: LegEnd, sink: LegEnd, viaFlag: PlanFlag): Observable<PlanLeg> {
    return this.context.legRepository.planLeg(this.context.networkType, source, sink).pipe(
      map(planLegDetail => {
        if (planLegDetail) {
          const lastRoute = planLegDetail.routes.last(null);
          if (lastRoute) {
            const legKey = PlanUtil.key(source, sink);
            const sinkFlagType = PlanFlagType.Via;
            const sinkFlag = new PlanFlag(sinkFlagType, FeatureId.next(), lastRoute.sinkNode.coordinate);
            const newLeg = new PlanLeg(FeatureId.next(), legKey, source, sink, sinkFlag, viaFlag, planLegDetail.routes);
            this.context.legs.add(newLeg);
            return newLeg;
          }
        }
        return null;
      })
    );
  }

  private buildExtraLeg(sourceNodeId: string, sinkNodeId: string, sinkFlagType: PlanFlagType): Observable<PlanLeg> {

    const source = PlanUtil.legEndNode(+sourceNodeId);
    const sink = PlanUtil.legEndNode(+sinkNodeId);

    return this.context.legRepository.planLeg(this.context.networkType, source, sink).pipe(
      map(planLegDetail => {
        if (planLegDetail) {
          const lastRoute = planLegDetail.routes.last(null);
          if (lastRoute) {
            const legKey = PlanUtil.key(source, sink);
            const sinkFlag = new PlanFlag(sinkFlagType, FeatureId.next(), lastRoute.sinkNode.coordinate);
            const newLeg = new PlanLeg(FeatureId.next(), legKey, source, sink, sinkFlag, null, planLegDetail.routes);
            this.context.legs.add(newLeg);
            return newLeg;
          }
        }
        return null;
      })
    );

  }

}
