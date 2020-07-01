import {List} from "immutable";
import {Plan} from "../../../kpn/api/common/planner/plan";
import {PlanFragment} from "../../../kpn/api/common/planner/plan-fragment";
import {PlanLeg} from "../../../kpn/api/common/planner/plan-leg";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {PlanRoute} from "../../../kpn/api/common/planner/plan-route";
import {PlanSegment} from "../../../kpn/api/common/planner/plan-segment";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {FeatureId} from "../features/feature-id";
import {PlanFlag} from "../plan/plan-flag";
import {PlanLegCache} from "../plan/plan-leg-cache";
import {PlanUtil} from "../plan/plan-util";
import {PlannerContext} from "./planner-context";
import {PlannerCursorMock} from "./planner-cursor-mock";
import {PlannerElasticBandMock} from "./planner-elastic-band-mock";
import {PlannerHighlightLayerMock} from "./planner-highlight-layer-mock";
import {PlannerLegRepositoryMock} from "./planner-leg-repository-mock";
import {PlannerRouteLayerMock} from "./planner-route-layer-mock";

export class PlannerTestSetup {

  readonly routeLayer = new PlannerRouteLayerMock();
  readonly cursor = new PlannerCursorMock();
  readonly elasticBand = new PlannerElasticBandMock();
  readonly highlightLayer = new PlannerHighlightLayerMock();
  readonly legRepository = new PlannerLegRepositoryMock();
  readonly legs = new PlanLegCache();

  readonly context = new PlannerContext(
    this.routeLayer,
    this.cursor,
    this.elasticBand,
    this.highlightLayer,
    this.legRepository,
    this.legs,
    null
  );

  readonly node1 = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);
  readonly node2 = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 2]);
  readonly node3 = PlanUtil.planNodeWithCoordinate("1003", "03", [3, 3]);
  readonly node4 = PlanUtil.planNodeWithCoordinate("1004", "04", [4, 4]);

  constructor() {
    this.context.nextNetworkType(NetworkType.hiking);
  }

  createLeg(sourceNode: PlanNode, sinkNode: PlanNode): PlanLeg {
    const fragment = new PlanFragment(0, 0, -1, sinkNode.coordinate, sinkNode.latLon);
    const segment = new PlanSegment(0, "", null, List([fragment]));
    const route = new PlanRoute(sourceNode, sinkNode, 0, List([segment]), List());
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    const legKey = PlanUtil.key(source, sink);
    const leg = new PlanLeg(FeatureId.next(), legKey, source, sink, sourceNode, sinkNode, 0, List([route]));
    this.legRepository.add(leg);
    this.legs.add(leg);
    return leg;
  }

  createPlanWithStartPointOnly(): Plan {
    const plan = PlanUtil.plan(this.node1, List());
    this.context.updatePlan(plan);
    this.routeLayer.addFlag(PlanFlag.fromStartNode(this.node1));
    return plan;
  }

  createOneLegPlan(): Plan {

    const leg = this.createLeg(this.node1, this.node2);
    const plan = PlanUtil.plan(this.node1, List([leg]));
    this.context.updatePlan(plan);

    this.routeLayer.addFlag(PlanFlag.fromStartNode(this.node1));
    this.routeLayer.addFlag(PlanFlag.fromViaNode(this.node2));
    this.routeLayer.addRouteLeg(leg);

    return plan;
  }

  createTwoLegPlan(): Plan {

    const leg1 = this.createLeg(this.node1, this.node2);
    const leg2 = this.createLeg(this.node2, this.node3);
    const plan = PlanUtil.plan(this.node1, List([leg1, leg2]));
    this.context.updatePlan(plan);

    this.routeLayer.addFlag(PlanFlag.fromStartNode(this.node1));
    this.routeLayer.addFlag(PlanFlag.fromViaNode(this.node2));
    this.routeLayer.addFlag(PlanFlag.fromViaNode(this.node3));
    this.routeLayer.addRouteLeg(leg1);
    this.routeLayer.addRouteLeg(leg2);

    return plan;
  }

}
