import {List} from "immutable";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {Plan} from "../plan/plan";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";
import {PlannerCommandMoveFirstLegSource} from "./planner-command-move-first-leg-source";

describe("PlannerCommandMoveFirstLegSource", () => {

  it("move start point - do and undo", () => {

    const setup = new PlannerTestSetup();

    const node1 = PlanNode.withCoordinate("1001", "01", [1, 1]);
    const node2 = PlanNode.withCoordinate("1002", "02", [2, 2]);
    const node3 = PlanNode.withCoordinate("1003", "03", [3, 3]);

    const oldLeg = new PlanLeg("12", "", node1, node2, 0, List());
    const newLeg = new PlanLeg("32", "", node3, node2, 0, List());

    setup.legs.add(oldLeg);
    setup.legs.add(newLeg);

    const plan = Plan.create(node1, List([oldLeg]));
    setup.context.updatePlan(plan);

    const command = new PlannerCommandMoveFirstLegSource("12", "32");
    setup.context.execute(command);

    expect(setup.context.plan.source.nodeId).toEqual("1003");
    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("32");
    expect(setup.context.plan.legs.get(0).source.nodeId).toEqual("1003");
    expect(setup.context.plan.legs.get(0).sink.nodeId).toEqual("1002");

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, setup.context.plan.source.featureId, [3, 3]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("32", newLeg);

    command.undo(setup.context);

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, node1.featureId, [1, 1]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("12", oldLeg);

    expect(setup.context.plan.source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sink.nodeId).toEqual("1002");

  });

});
