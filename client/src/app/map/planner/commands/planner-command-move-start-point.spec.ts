import {List} from "immutable";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommandMoveStartPoint} from "./planner-command-move-start-point";

describe("PlannerCommandMoveStartPoint", () => {

  it("move start point - do and undo", () => {

    const setup = new PlannerTestSetup();

    const node1 = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);
    const node2 = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 2]);

    const sourceFlag = PlanFlag.start("f", node1);

    setup.routeLayer.addFlag(sourceFlag);

    const plan = new Plan(node1, sourceFlag, List());
    setup.context.updatePlan(plan);

    const command = new PlannerCommandMoveStartPoint(node1, node2);
    setup.context.execute(command);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1002");

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectStartFlagExists("f", [2, 2]);

    command.undo(setup.context);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectStartFlagExists("f", [1, 1]);

  });

});
