import {List} from "immutable";
import {Coordinate} from "ol/coordinate";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {PlanNode} from "../../../../kpn/api/common/planner/plan-node";
import {PlannerCommandMoveViaRoute} from "../../commands/planner-command-move-via-route";
import {PlannerContext} from "../../context/planner-context";
import {FeatureId} from "../../features/feature-id";
import {RouteFeature} from "../../features/route-feature";
import {PlanFlag} from "../../plan/plan-flag";
import {PlanFlagType} from "../../plan/plan-flag-type";
import {PlanLeg} from "../../plan/plan-leg";
import {PlanUtil} from "../../plan/plan-util";

export class DropViaRouteOnRoute {

  constructor(private readonly context: PlannerContext) {
  }

  drop(oldLeg: PlanLeg, routeFeatures: List<RouteFeature>, coordinate: Coordinate): void {
    this.buildViaRouteLeg(oldLeg, routeFeatures, coordinate).subscribe(newLeg1 => {
      if (newLeg1) {
        this.buildNodeToNodeLeg(newLeg1.sinkNode, oldLeg.sinkNode, oldLeg.sinkFlag, coordinate).subscribe(newLeg2 => {
          if (newLeg2) {
            const command = new PlannerCommandMoveViaRoute(
              oldLeg.featureId,
              newLeg1.featureId,
              newLeg2.featureId
            );
            this.context.execute(command);
          }
        });
      }
    });
  }

  private buildViaRouteLeg(oldLeg: PlanLeg, routeFeatures: List<RouteFeature>, coordinate: Coordinate): Observable<PlanLeg> {

    const source = PlanUtil.legEndNode(+oldLeg.sourceNode.nodeId);
    const sink = PlanUtil.legEndRoutes(routeFeatures);

    return this.context.legRepository.planLeg(this.context.networkType, source, sink).pipe(
      map(data => {
        const legKey = PlanUtil.key(source, sink);
        const viaFlag = new PlanFlag(PlanFlagType.Via, FeatureId.next(), coordinate);
        const sinkFlag = new PlanFlag(PlanFlagType.Invisible, FeatureId.next(), coordinate);
        const newLeg = new PlanLeg(FeatureId.next(), legKey, source, sink, sinkFlag, viaFlag, data.routes);
        this.context.legs.add(newLeg);
        return newLeg;
      })
    );
  }

  private buildNodeToNodeLeg(sourceNode: PlanNode, sinkNode: PlanNode, sinkFlag: PlanFlag, coordinate: Coordinate): Observable<PlanLeg> {

    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);

    return this.context.legRepository.planLeg(this.context.networkType, source, sink).pipe(
      map(data => {
        const legKey = PlanUtil.key(source, sink);
        const leg = new PlanLeg(FeatureId.next(), legKey, source, sink, sinkFlag, null, data.routes);
        this.context.legs.add(leg);
        return leg;
      })
    );
  }

}
