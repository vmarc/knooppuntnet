import {List} from "immutable";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanNode} from "../plan/plan-node";
import {PlannerCommandMoveStartPoint} from "./planner-command-move-start-point";

describe("PlannerCommandMoveStartPoint", () => {

  it("move start point - do and undo", () => {

    const setup = new PlannerTestSetup();

    const node1 = PlanNode.create("1001", "01", [1, 1]);
    const node2 = PlanNode.create("1002", "02", [2, 2]);

    setup.routeLayer.addFlag(PlanFlag.fromStartNode(node1));

    const plan = Plan.create(node1, List());
    setup.context.updatePlan(plan);

    const command = new PlannerCommandMoveStartPoint(node1, node2);
    setup.context.execute(command);

    expect(setup.context.plan.source.nodeId).toEqual("1002");

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, setup.context.plan.source.featureId, [2, 2]);

    command.undo(setup.context);

    expect(setup.context.plan.source.nodeId).toEqual("1001");

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, setup.context.plan.source.featureId, [1, 1]);

  });

});
