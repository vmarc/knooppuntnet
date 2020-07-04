import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlanFlag} from "../plan/plan-flag";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommandAddStartPoint} from "./planner-command-add-start-point";

describe("PlannerCommandAddStartPoint", () => {

  it("add start point - do and undo", () => {

    const setup = new PlannerTestSetup();
    const node = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);

    const command = new PlannerCommandAddStartPoint(node, PlanFlag.start("n1", node));
    setup.context.execute(command);

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectStartFlagExists("n1", [1, 1]);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.size).toEqual(0);

    command.undo(setup.context);

    setup.routeLayer.expectFlagCount(0);
    expect(setup.context.plan.sourceNode).toEqual(null);
    expect(setup.context.plan.legs.size).toEqual(0);
  });

});
