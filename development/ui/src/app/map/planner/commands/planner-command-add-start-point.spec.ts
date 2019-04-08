import {PlannerContext} from "../context/planner-context";
import {PlannerCrosshairMock} from "../context/planner-crosshair-mock";
import {PlannerElasticBandMock} from "../context/planner-elastic-band-mock";
import {PlannerRouteLayerMock} from "../context/planner-route-layer-mock";
import {Plan} from "../plan/plan";
import {PlanLegCache} from "../plan/plan-leg-cache";
import {PlanNode} from "../plan/plan-node";
import {PlannerCommandAddStartPoint} from "./planner-command-add-start-point";
import {PlannerCommandStackImpl} from "./planner-command-stack-impl";

describe("PlannerCommandAddStartPoint", () => {

  it("add start point - do and undo", () => {

    const coordinate1 = [1, 1];
    const node1: PlanNode = new PlanNode("1001", "01", coordinate1);

    const command = new PlannerCommandAddStartPoint(node1);

    const commandStack = new PlannerCommandStackImpl();
    const routeLayer = new PlannerRouteLayerMock();
    const crosshair = new PlannerCrosshairMock();
    const elasticBand = new PlannerElasticBandMock();

    const context = new PlannerContext(commandStack, routeLayer, crosshair, elasticBand, new PlanLegCache());

    command.do(context);

    expect(routeLayer.startNodeCount()).toEqual(1);
    routeLayer.expectStartNodeExists("1001");

    expect(context.plan.source.nodeId).toEqual("1001");
    expect(context.plan.legs.size).toEqual(0);

    command.undo(context);

    expect(routeLayer.startNodeCount()).toEqual(0);

    expect(context.plan.source).toEqual(null);
    expect(context.plan.legs.size).toEqual(0);
  });

});
