import {List} from "immutable";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {FeatureId} from "../features/feature-id";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanUtil} from "../plan/plan-util";
import {PlannerContext} from "./planner-context";
import {PlannerCursorMock} from "./planner-cursor-mock";
import {PlannerElasticBandMock} from "./planner-elastic-band-mock";
import {PlannerHighlightLayerMock} from "./planner-highlight-layer-mock";
import {PlannerLegRepositoryMock} from "./planner-leg-repository-mock";
import {PlannerMarkerLayerMock} from "./planner-marker-layer-mock";
import {PlannerRouteLayerMock} from "./planner-route-layer-mock";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {PlanRoute} from "../../../kpn/api/common/planner/plan-route";
import {PlanLegData} from "./plan-leg-data";

export class PlannerTestSetup {

  readonly routeLayer = new PlannerRouteLayerMock();
  readonly markerLayer = new PlannerMarkerLayerMock();
  readonly cursor = new PlannerCursorMock();
  readonly elasticBand = new PlannerElasticBandMock();
  readonly highlightLayer = new PlannerHighlightLayerMock();
  readonly legRepository = new PlannerLegRepositoryMock();

  readonly context = new PlannerContext(
    this.routeLayer,
    this.markerLayer,
    this.cursor,
    this.elasticBand,
    this.highlightLayer,
    this.legRepository,
    null
  );

  readonly node1 = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);
  readonly node2 = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 2]);
  readonly node3 = PlanUtil.planNodeWithCoordinate("1003", "03", [3, 3]);
  readonly node4 = PlanUtil.planNodeWithCoordinate("1004", "04", [4, 4]);

  constructor() {
    this.context.nextNetworkType(NetworkType.hiking);
  }

  createPlanWithStartPointOnly(): Plan {
    const sourceFlag = PlanFlag.start("sourceFlag", this.node1.coordinate);
    const plan = new Plan(this.node1, sourceFlag, List());
    this.context.updatePlan(plan);
    this.markerLayer.addFlag(sourceFlag);
    return plan;
  }

  createOneLegPlan(): Plan {

    const sourceFlag = PlanFlag.start("sourceFlag", this.node1.coordinate);
    const sinkFlag = PlanFlag.end("sinkFlag", this.node2.coordinate);
    const leg = PlanUtil.singleRoutePlanLeg(FeatureId.next(), this.node1, this.node2, sinkFlag, null);
    const plan = new Plan(this.node1, sourceFlag, List([leg]));
    this.context.updatePlan(plan);

    this.markerLayer.addFlag(sourceFlag);
    this.markerLayer.addFlag(sinkFlag);
    this.routeLayer.addPlanLeg(leg);

    return plan;
  }

  createTwoLegPlan(): Plan {

    const sourceFlag = PlanFlag.start("sourceFlag", this.node1.coordinate);
    const sinkFlag1 = PlanFlag.via("sinkFlag1", this.node2.coordinate);
    const sinkFlag2 = PlanFlag.end("sinkFlag2", this.node3.coordinate);

    const leg1 = PlanUtil.singleRoutePlanLeg(FeatureId.next(), this.node1, this.node2, sinkFlag1, null);
    const leg2 = PlanUtil.singleRoutePlanLeg(FeatureId.next(), this.node2, this.node3, sinkFlag2, null);

    const plan = new Plan(this.node1, sourceFlag, List([leg1, leg2]));
    this.context.updatePlan(plan);

    this.markerLayer.addFlag(sourceFlag);
    this.markerLayer.addFlag(sinkFlag1);
    this.markerLayer.addFlag(sinkFlag2);
    this.routeLayer.addPlanLeg(leg1);
    this.routeLayer.addPlanLeg(leg2);

    return plan;
  }

  createThreeLegPlan(): Plan {

    const sourceFlag = PlanFlag.start("sourceFlag", this.node1.coordinate);
    const sinkFlag1 = PlanFlag.via("sinkFlag1", this.node2.coordinate);
    const sinkFlag2 = PlanFlag.via("sinkFlag2", this.node3.coordinate);
    const sinkFlag3 = PlanFlag.end("sinkFlag3", this.node4.coordinate);

    const leg1 = PlanUtil.singleRoutePlanLeg(FeatureId.next(), this.node1, this.node2, sinkFlag1, null);
    const leg2 = PlanUtil.singleRoutePlanLeg(FeatureId.next(), this.node2, this.node3, sinkFlag2, null);
    const leg3 = PlanUtil.singleRoutePlanLeg(FeatureId.next(), this.node3, this.node4, sinkFlag3, null);

    const plan = new Plan(this.node1, sourceFlag, List([leg1, leg2, leg3]));
    this.context.updatePlan(plan);

    this.markerLayer.addFlag(sourceFlag);
    this.markerLayer.addFlag(sinkFlag1);
    this.markerLayer.addFlag(sinkFlag2);
    this.markerLayer.addFlag(sinkFlag3);
    this.routeLayer.addPlanLeg(leg1);
    this.routeLayer.addPlanLeg(leg2);
    this.routeLayer.addPlanLeg(leg3);

    return plan;
  }

  createPlanLegData(node1: PlanNode, node2: PlanNode): PlanLegData {
    const source = PlanUtil.legEndNode(+node1.nodeId);
    const sink = PlanUtil.legEndNode(+node2.nodeId);
    const planRoute = new PlanRoute(node1, node2, 0, List(), List());
    const planLegData = new PlanLegData(source, sink, List([planRoute]));
    this.legRepository.add(planLegData);
    return planLegData
  }

}
