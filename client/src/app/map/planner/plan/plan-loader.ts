import {List} from "immutable";
import {Observable} from "rxjs";
import {combineLatest} from "rxjs";
import {map} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LegBuildParams} from "../../../kpn/api/common/planner/leg-build-params";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";
import {LegEndNode} from "../../../kpn/api/common/planner/leg-end-node";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {FeatureId} from "../features/feature-id";
import {Plan} from "./plan";
import {PlanLeg} from "./plan-leg";
import {PlanLegBuilder} from "./plan-leg-builder";
import {PlanLegNodeIds} from "./plan-leg-node-ids";
import {PlanRoute} from "./plan-route";
import {PlanUtil} from "./plan-util";

/*
  Takes a string representation of a plan and converts it in a Plan object.
  Retrieves the route details from the server.
 */
export class PlanLoader {

  constructor(private appService: AppService) {
  }

  load(networkType: NetworkType, planString: string): Observable<Plan> {

    // TODO PLAN take via-routes into account
    const nodeIds = PlanUtil.toNodeIds(planString);
    let legNodeIdss: List<PlanLegNodeIds> = List();
    for (let i = 0; i < nodeIds.size - 1; i++) {
      legNodeIdss = legNodeIdss.push(new PlanLegNodeIds(nodeIds.get(i), nodeIds.get(i + 1)));
    }

    const legObservables = legNodeIdss.map(legNodeIds => {
      const params = new LegBuildParams(
        networkType.name,
        FeatureId.next(),
        new LegEnd(new LegEndNode(+legNodeIds.sourceNodeId), null),
        new LegEnd(new LegEndNode(+legNodeIds.sinkNodeId), null)
      );
      return this.appService.routeLeg(params).pipe(
        map(response => PlanLegBuilder.toPlanLeg2(response.result))
      );
    }).toArray();

    const legsObservable = combineLatest(legObservables);
    return legsObservable.pipe(
      map(legs => {
        for (let i = 1; i < legs.length; i++) {
          let newRoutes: List<PlanRoute> = List();
          for (let j = 0; j < legs[i].routes.size; j++) {
            const oldRoute = legs[i].routes.get(j);
            const newSource = j === 0 ? legs[i - 1].sinkNode : oldRoute.sourceNode;
            newRoutes = newRoutes.push(
              new PlanRoute(
                newSource,
                oldRoute.sinkNode,
                oldRoute.meters,
                oldRoute.segments,
                oldRoute.streets
              )
            );
          }

          const source = PlanUtil.legEndNode(+legs[i - 1].sinkNode.nodeId);
          const sink = PlanUtil.legEndNode(+legs[i].sinkNode.nodeId);
          const legKey = PlanUtil.key(source, sink);

          legs[i] = new PlanLeg( // TODO should add to new collection instead of mutating existing array!
            legs[i].featureId,
            legKey,
            source,
            sink,
            legs[i - 1].sinkNode,
            legs[i].sinkNode,
            legs[i].meters,
            newRoutes
          );
        }
        return Plan.create(legs[0].sourceNode, List(legs));
      })
    );
  }
}
