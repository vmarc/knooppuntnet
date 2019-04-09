import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlanNode} from "../plan/plan-node";
import {PlannerCommandAddStartPoint} from "./planner-command-add-start-point";

describe("PlannerCommandAddStartPoint", () => {

  it("add start point - do and undo", () => {

    const setup = new PlannerTestSetup();
    const node = new PlanNode("1001", "01", [1, 1]);

    const command = new PlannerCommandAddStartPoint(node);
    setup.context.execute(command);

    setup.routeLayer.expectStartNodeCount(1);
    setup.routeLayer.expectStartNodeExists("1001", [1, 1]);

    expect(setup.context.plan.source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.size).toEqual(0);

    command.undo(setup.context);

    setup.routeLayer.expectStartNodeCount(0);
    expect(setup.context.plan.source).toEqual(null);
    expect(setup.context.plan.legs.size).toEqual(0);
  });

});
