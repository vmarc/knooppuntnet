import {List} from "immutable";
import {PlannerCommandStack} from "../commands/planner-command-stack";
import {FeatureId} from "../features/feature-id";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanFragment} from "../plan/plan-fragment";
import {PlanLeg} from "../plan/plan-leg";
import {PlanLegCache} from "../plan/plan-leg-cache";
import {PlanNode} from "../plan/plan-node";
import {PlanRoute} from "../plan/plan-route";
import {PlanSegment} from "../plan/plan-segment";
import {PlannerContext} from "./planner-context";
import {PlannerCrosshairMock} from "./planner-crosshair-mock";
import {PlannerCursorMock} from "./planner-cursor-mock";
import {PlannerElasticBandMock} from "./planner-elastic-band-mock";
import {PlannerLegRepositoryMock} from "./planner-leg-repository-mock";
import {PlannerRouteLayerMock} from "./planner-route-layer-mock";

export class PlannerTestSetup {
  readonly commandStack = new PlannerCommandStack();
  readonly routeLayer = new PlannerRouteLayerMock();
  readonly crosshair = new PlannerCrosshairMock();
  readonly cursor = new PlannerCursorMock();
  readonly elasticBand = new PlannerElasticBandMock();
  readonly legRepository = new PlannerLegRepositoryMock();
  readonly legs = new PlanLegCache();
  readonly context = new PlannerContext(
    this.commandStack,
    this.routeLayer,
    this.crosshair,
    this.cursor,
    this.elasticBand,
    this.legRepository,
    this.legs
  );

  readonly node1 = PlanNode.create("1001", "01", [1, 1]);
  readonly node2 = PlanNode.create("1002", "02", [2, 2]);
  readonly node3 = PlanNode.create("1003", "03", [3, 3]);
  readonly node4 = PlanNode.create("1004", "04", [4, 4]);

  createLeg(source: PlanNode, sink: PlanNode): PlanLeg {
    const fragment = new PlanFragment(0, 0, -1, sink.coordinate);
    const segment = new PlanSegment(0, "", List([fragment]));
    const route = new PlanRoute(source, sink, 0, List([segment]), List());
    const leg = new PlanLeg(FeatureId.next(), source, sink, 0, List([route]));
    this.legRepository.add(leg);
    this.legs.add(leg);
    return leg;
  }

  createPlanWithStartPointOnly(): Plan {
    const plan = new Plan(this.node1, List());
    this.context.updatePlan(plan);
    this.routeLayer.addFlag(PlanFlag.fromStartNode(this.node1));
    return plan;
  }

  createOneLegPlan(): Plan {

    const leg = this.createLeg(this.node1, this.node2);
    const plan = new Plan(this.node1, List([leg]));
    this.context.updatePlan(plan);

    this.routeLayer.addFlag(PlanFlag.fromStartNode(this.node1));
    this.routeLayer.addFlag(PlanFlag.fromViaNode(this.node2));
    this.routeLayer.addRouteLeg(leg);

    return plan;
  }

  createTwoLegPlan(): Plan {

    const leg1 = this.createLeg(this.node1, this.node2);
    const leg2 = this.createLeg(this.node2, this.node3);
    const plan = new Plan(this.node1, List([leg1, leg2]));
    this.context.updatePlan(plan);

    this.routeLayer.addFlag(PlanFlag.fromStartNode(this.node1));
    this.routeLayer.addFlag(PlanFlag.fromViaNode(this.node2));
    this.routeLayer.addFlag(PlanFlag.fromViaNode(this.node3));
    this.routeLayer.addRouteLeg(leg1);
    this.routeLayer.addRouteLeg(leg2);

    return plan;
  }

}
