import {List} from "immutable";
import {PlannerCommandStack} from "../commands/planner-command-stack";
import {FeatureId} from "../features/feature-id";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanLeg} from "../plan/plan-leg";
import {PlanLegCache} from "../plan/plan-leg-cache";
import {PlanLegFragment} from "../plan/plan-leg-fragment";
import {PlanNode} from "../plan/plan-node";
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

  createPlanWithStartPointOnly(): Plan {
    const startNode = PlanNode.create("1001", "01", [1, 1]);
    const plan = new Plan(startNode, List());
    this.context.updatePlan(plan);
    this.routeLayer.addFlag(PlanFlag.fromStartNode(startNode));
    return plan;
  }

  createOneLegPlan(): Plan {

    const startNode = PlanNode.create("1001", "01", [1, 1]);
    const endNode = PlanNode.create("1002", "02", [2, 2]);
    const fragment = new PlanLegFragment(endNode, 10, List([startNode.coordinate, endNode.coordinate]));
    const leg = new PlanLeg(FeatureId.next(), startNode, endNode, List([fragment]));
    this.context.legs.add(leg);
    const plan = new Plan(startNode, List([leg]));
    this.context.updatePlan(plan);

    this.routeLayer.addFlag(PlanFlag.fromStartNode(startNode));
    this.routeLayer.addFlag(PlanFlag.fromViaNode(endNode));
    this.routeLayer.addRouteLeg(leg);

    return plan;
  }

  createTwoLegPlan(): Plan {

    const startNode = PlanNode.create("1001", "01", [1, 1]);
    const viaNode = PlanNode.create("1002", "02", [2, 2]);
    const endNode = PlanNode.create("1003", "03", [3, 3]);

    const fragment1 = new PlanLegFragment(viaNode, 10, List([startNode.coordinate, viaNode.coordinate]));
    const leg1 = new PlanLeg(FeatureId.next(), startNode, viaNode, List([fragment1]));
    this.context.legs.add(leg1);

    const fragment2 = new PlanLegFragment(endNode, 10, List([viaNode.coordinate, endNode.coordinate]));
    const leg2 = new PlanLeg(FeatureId.next(), viaNode, endNode, List([fragment2]));
    this.context.legs.add(leg2);

    const plan = new Plan(startNode, List([leg1, leg2]));
    this.context.updatePlan(plan);

    this.routeLayer.addFlag(PlanFlag.fromStartNode(startNode));
    this.routeLayer.addFlag(PlanFlag.fromViaNode(viaNode));
    this.routeLayer.addFlag(PlanFlag.fromViaNode(endNode));
    this.routeLayer.addRouteLeg(leg1);
    this.routeLayer.addRouteLeg(leg2);

    return plan;
  }

}
