import {List} from "immutable";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlannerCommandMoveStartPoint} from "./planner-command-move-start-point";

describe("PlannerCommandMoveStartPoint", () => {

  it("move start point - do and undo", () => {

    const setup = new PlannerTestSetup();

    const sourceFlag = PlanFlag.start("sourceFlag", [1, 1]);

    setup.markerLayer.addFlag(sourceFlag);

    const plan = new Plan(setup.node1, sourceFlag, List());
    setup.context.updatePlan(plan);

    const command = new PlannerCommandMoveStartPoint(setup.node1, setup.node2);
    setup.context.execute(command);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1002");
    setup.markerLayer.expectFlagCount(1);
    setup.markerLayer.expectStartFlagExists("sourceFlag", [2, 2]);

    command.undo(setup.context);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");
    setup.markerLayer.expectFlagCount(1);
    setup.markerLayer.expectStartFlagExists("sourceFlag", [1, 1]);

    command.do(setup.context);

    expect(setup.context.plan.sourceNode.nodeId).toEqual("1002");
    setup.markerLayer.expectFlagCount(1);
    setup.markerLayer.expectStartFlagExists("sourceFlag", [2, 2]);
  });

});
