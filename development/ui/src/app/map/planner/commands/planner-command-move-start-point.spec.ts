import {List} from "immutable";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";
import {PlannerCommandMoveStartPoint} from "./planner-command-move-start-point";

describe("PlannerCommandMoveStartPoint", () => {

  it("move start point - do and undo", () => {

    const setup = new PlannerTestSetup();

    const node1 = new PlanNode("1001", "01", [1, 1]);
    const node2 = new PlanNode("1002", "02", [2, 2]);
    const node3 = new PlanNode("1003", "03", [3, 3]);

    const oldLeg = new PlanLeg("12", node1, node2, List());
    const newLeg = new PlanLeg("32", node3, node2, List());

    setup.legs.add(oldLeg);
    setup.legs.add(newLeg);

    const plan = new Plan(node1, List([oldLeg]));
    setup.context.updatePlan(plan);

    const command = new PlannerCommandMoveStartPoint("12", "32");
    setup.context.execute(command);

    setup.routeLayer.expectStartNodeCount(1);
    setup.routeLayer.expectStartNodeExists("1003", [3, 3]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("32", newLeg);

    expect(setup.context.plan.source.nodeId).toEqual("1003");
    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).legId).toEqual("32");
    expect(setup.context.plan.legs.get(0).source.nodeId).toEqual("1003");
    expect(setup.context.plan.legs.get(0).sink.nodeId).toEqual("1002");

    command.undo(setup.context);

    setup.routeLayer.expectStartNodeCount(1);
    setup.routeLayer.expectStartNodeExists("1001", [1, 1]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("12", oldLeg);

    expect(setup.context.plan.source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).legId).toEqual("12");
    expect(setup.context.plan.legs.get(0).source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sink.nodeId).toEqual("1002");

  });

});
