import {List} from "immutable";
import {PlannerContext} from "../context/planner-context";
import {PlannerCrosshairMock} from "../context/planner-crosshair-mock";
import {PlannerElasticBandMock} from "../context/planner-elastic-band-mock";
import {PlannerRouteLayerMock} from "../context/planner-route-layer-mock";
import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlanLegCache} from "../plan/plan-leg-cache";
import {PlanNode} from "../plan/plan-node";
import {PlannerCommandSplitLeg} from "./planner-command-split-leg";
import {PlannerCommandStackImpl} from "./planner-command-stack-impl";

describe("PlannerCommandSplitLeg", () => {

  it("split leg - do and undo", () => {

    const coordinate1 = [1, 1];
    const coordinate2 = [2, 2];
    const coordinate3 = [3, 3];

    const node1: PlanNode = new PlanNode("1001", "01", coordinate1);
    const node2: PlanNode = new PlanNode("1002", "02", coordinate2);
    const node3: PlanNode = new PlanNode("1003", "03", coordinate3);

    const oldLeg: PlanLeg = new PlanLeg("12", node1, node2, List());
    const newLeg1: PlanLeg = new PlanLeg("13", node1, node3, List());
    const newLeg2: PlanLeg = new PlanLeg("32", node3, node2, List());

    const legs = new PlanLegCache();
    legs.add(oldLeg);
    legs.add(newLeg1);
    legs.add(newLeg2);

    const command = new PlannerCommandSplitLeg(oldLeg.legId, newLeg1.legId, newLeg2.legId);

    const commandStack = new PlannerCommandStackImpl();
    const routeLayer = new PlannerRouteLayerMock();
    const crosshair = new PlannerCrosshairMock();
    const elasticBand = new PlannerElasticBandMock();

    const context = new PlannerContext(commandStack, routeLayer, crosshair, elasticBand, legs);

    const plan = new Plan(node1, List([oldLeg]));
    context.routeLayer.addRouteLeg(oldLeg);
    context.updatePlan(plan);

    expect(routeLayer.routeLegCount()).toBe(1);
    routeLayer.expectRouteLegExists("12");

    command.do(context);

    expect(routeLayer.viaNodeCount()).toBe(1);
    routeLayer.expectViaNodeExists("13", "1003");

    expect(routeLayer.routeLegCount()).toBe(2);
    routeLayer.expectRouteLegExists("13");
    routeLayer.expectRouteLegExists("32");

    expect(context.plan.source.nodeId).toEqual("1001");
    expect(context.plan.legs.get(0).legId).toEqual("13");
    expect(context.plan.legs.get(0).source.nodeId).toEqual("1001");
    expect(context.plan.legs.get(0).sink.nodeId).toEqual("1003");
    expect(context.plan.legs.get(1).legId).toEqual("32");
    expect(context.plan.legs.get(1).source.nodeId).toEqual("1003");
    expect(context.plan.legs.get(1).sink.nodeId).toEqual("1002");

  });

});
