import {List} from "immutable";
import {Coordinate} from "ol/coordinate";
import {Observable} from "rxjs";
import {map, switchMap} from "rxjs/operators";
import {PlanNode} from "../../../../kpn/api/common/planner/plan-node";
import {PlannerCommandMoveViaPointToViaRoute} from "../../commands/planner-command-move-via-point-to-via-route";
import {PlannerContext} from "../../context/planner-context";
import {FeatureId} from "../../features/feature-id";
import {RouteFeature} from "../../features/route-feature";
import {PlanFlag} from "../../plan/plan-flag";
import {PlanFlagType} from "../../plan/plan-flag-type";
import {PlanLeg} from "../../plan/plan-leg";
import {PlanUtil} from "../../plan/plan-util";
import {PlannerDragFlag} from "../planner-drag-flag";

export class DropViaNodeOnRoute {

  constructor(private readonly context: PlannerContext) {
  }

  drop(dragFlag: PlannerDragFlag, routeFeatures: List<RouteFeature>, coordinate: Coordinate): void {

    const legs = this.context.plan.legs;
    const legIndex2 = legs.findIndex(leg => leg.sourceNode.featureId === dragFlag.oldNode.featureId);
    const oldLeg1 = legs.get(legIndex2 - 1);
    const oldLeg2 = legs.get(legIndex2);

    this.buildNewLeg1(oldLeg1, routeFeatures, coordinate).pipe(
      switchMap(newLeg1 =>
        this.buildNewLeg2(newLeg1.sinkNode, oldLeg2.sinkNode, oldLeg2.sinkFlag, coordinate).pipe(
          map(newLeg2 => new PlannerCommandMoveViaPointToViaRoute(oldLeg1, oldLeg2, newLeg1, newLeg2))
        )
      )
    ).subscribe(
      command => this.context.execute(command),
      error => {
        this.context.resetDragFlag(dragFlag);
        this.context.errorDialog(error);
      }
    );
  }

  private buildNewLeg1(oldLeg1: PlanLeg, routeFeatures: List<RouteFeature>, coordinate: Coordinate): Observable<PlanLeg> {

    const source = PlanUtil.legEndNode(+oldLeg1.sourceNode.nodeId);
    const sink = PlanUtil.legEndRoutes(routeFeatures);
    const viaFlag = new PlanFlag(PlanFlagType.Via, FeatureId.next(), coordinate);
    const sinkFlag = new PlanFlag(PlanFlagType.Invisible, FeatureId.next(), coordinate);

    return this.context.fetchLeg(source, sink).pipe(
      map(data => PlanUtil.leg(data, sinkFlag, viaFlag))
    );
  }

  private buildNewLeg2(sourceNode: PlanNode, sinkNode: PlanNode, sinkFlag: PlanFlag, coordinate: Coordinate): Observable<PlanLeg> {

    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    const viaFlag = new PlanFlag(PlanFlagType.Via, FeatureId.next(), coordinate);

    return this.context.fetchLeg(source, sink).pipe(
      map(data => PlanUtil.leg(data, sinkFlag, viaFlag))
    );
  }

}
