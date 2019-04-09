import {List} from "immutable";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";
import {PlannerCommandAddLeg} from "./planner-command-add-leg";

describe("PlannerCommandAddLeg", () => {

  it("do and undo", () => {

    const node1 = new PlanNode("1001", "01", [1, 1]);
    const node2 = new PlanNode("1002", "02", [2, 2]);
    const leg = new PlanLeg("12", node1, node2, List());
    const plan = new Plan(node1, List());

    const setup = new PlannerTestSetup();
    setup.context.updatePlan(plan);
    setup.legs.add(leg);

    const command = new PlannerCommandAddLeg(leg.legId);
    setup.context.execute(command);

    setup.routeLayer.expectViaNodeCount(1);
    setup.routeLayer.expectViaNodeExists("12", "1002", [2, 2]);
    setup.routeLayer.expectRouteLegExists("12", leg);

    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).legId).toEqual("12");
    expect(setup.context.plan.legs.get(0).source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sink.nodeId).toEqual("1002");

    command.undo(setup.context);

    setup.routeLayer.expectViaNodeCount(0);
    setup.routeLayer.expectRouteLegCount(0);
    expect(setup.context.plan.legs.size).toEqual(0);
  });

});
