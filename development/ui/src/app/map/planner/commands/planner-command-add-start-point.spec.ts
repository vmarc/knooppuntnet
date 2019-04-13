import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanNode} from "../plan/plan-node";
import {PlannerCommandAddStartPoint} from "./planner-command-add-start-point";

describe("PlannerCommandAddStartPoint", () => {

  it("add start point - do and undo", () => {

    const setup = new PlannerTestSetup();
    const node = PlanNode.create("1001", "01", [1, 1]);

    const command = new PlannerCommandAddStartPoint(node);
    setup.context.execute(command);

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, node.featureId, [1, 1]);

    expect(setup.context.plan.source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.size).toEqual(0);

    command.undo(setup.context);

    setup.routeLayer.expectFlagCount(0);
    expect(setup.context.plan.source).toEqual(null);
    expect(setup.context.plan.legs.size).toEqual(0);
  });

});
