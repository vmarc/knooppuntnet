import {List} from "immutable";
import {Coordinate} from "ol/coordinate";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
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

  drop(nodeDrag: PlannerDragFlag, routeFeatures: List<RouteFeature>, coordinate: Coordinate): void {

    const legs = this.context.plan.legs;
    const legIndex2 = legs.findIndex(leg => leg.sourceNode.featureId === nodeDrag.oldNode.featureId);
    const oldLeg1 = legs.get(legIndex2 - 1);
    const oldLeg2 = legs.get(legIndex2);

    this.buildNewLeg1(oldLeg1, routeFeatures, coordinate).subscribe(newLeg1 => {
      if (newLeg1) {
        this.buildNewLeg2(newLeg1.sinkNode, oldLeg2.sinkNode, coordinate).subscribe(newLeg2 => {
          if (newLeg2) {
            const command = new PlannerCommandMoveViaPointToViaRoute(
              oldLeg1.featureId,
              oldLeg2.featureId,
              newLeg1.featureId,
              newLeg2.featureId
            );
            this.context.execute(command);
          }
        });
      }
    });
  }

  private buildNewLeg1(oldLeg1: PlanLeg, routeFeatures: List<RouteFeature>, coordinate: Coordinate): Observable<PlanLeg> {

    const source = PlanUtil.legEndNode(+oldLeg1.sourceNode.nodeId);
    const sink = PlanUtil.legEndRoutes(routeFeatures);

    return this.context.legRepository.planLeg(this.context.networkType, source, sink).pipe(
      map(data => {
        const legKey = PlanUtil.key(source, sink);
        const viaFlag = new PlanFlag(PlanFlagType.Via, FeatureId.next(), coordinate);
        const sinkFlag = new PlanFlag(PlanFlagType.Invisible, FeatureId.next(), coordinate);
        const newLeg1 = new PlanLeg(FeatureId.next(), legKey, source, sink, sinkFlag, viaFlag, data.routes);
        this.context.legs.add(newLeg1);
        return newLeg1;
      })
    );
  }

  private buildNewLeg2(sourceNode: PlanNode, sinkNode: PlanNode, coordinate: Coordinate): Observable<PlanLeg> {

    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);

    return this.context.legRepository.planLeg(this.context.networkType, source, sink).pipe(
      map(data => {
        const legKey = PlanUtil.key(source, sink);
        const viaFlag2 = new PlanFlag(PlanFlagType.Via, FeatureId.next(), coordinate);
        const sinkFlag2 = new PlanFlag(PlanFlagType.Invisible, FeatureId.next(), coordinate);
        const newLeg2 = new PlanLeg(FeatureId.next(), legKey, source, sink, sinkFlag2, viaFlag2, data.routes);
        this.context.legs.add(newLeg2);
        return newLeg2;
      })
    );
  }

}
