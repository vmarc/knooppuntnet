import {List} from "immutable";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {Plan} from "../plan/plan";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";
import {PlannerCommandMoveViaPoint} from "./planner-command-move-via-point";

describe("PlannerCommandMoveViaPoint", () => {

  it("move via point - do and undo", () => {

    const setup = new PlannerTestSetup();

    const node1 = PlanNode.create("1001", "01", [1, 1]);
    const node2 = PlanNode.create("1002", "02", [2, 2]);
    const node3 = PlanNode.create("1003", "03", [3, 3]);
    const node4 = PlanNode.create("1004", "04", [4, 4]);

    const oldLeg1 = new PlanLeg("12", node1, node2, 0, List());
    const oldLeg2 = new PlanLeg("23", node2, node3, 0, List());
    const newLeg1 = new PlanLeg("14", node1, node4, 0, List());
    const newLeg2 = new PlanLeg("43", node4, node3, 0, List());

    setup.legs.add(oldLeg1);
    setup.legs.add(oldLeg2);
    setup.legs.add(newLeg1);
    setup.legs.add(newLeg2);

    const plan = new Plan(node1, List([oldLeg1, oldLeg2]));
    setup.context.updatePlan(plan);

    const command = new PlannerCommandMoveViaPoint(1, oldLeg1.featureId, oldLeg2.featureId, newLeg1.featureId, newLeg2.featureId);
    setup.context.execute(command);

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectFlagExists(PlanFlagType.Via, node4.featureId, [4, 4]);
    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("14", newLeg1);
    setup.routeLayer.expectRouteLegExists("43", newLeg2);

    expect(setup.context.plan.source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("14");
    expect(setup.context.plan.legs.get(0).source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sink.nodeId).toEqual("1004");
    expect(setup.context.plan.legs.get(1).featureId).toEqual("43");
    expect(setup.context.plan.legs.get(1).source.nodeId).toEqual("1004");
    expect(setup.context.plan.legs.get(1).sink.nodeId).toEqual("1003");

    command.undo(setup.context);

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectFlagExists(PlanFlagType.Via, node2.featureId, [2, 2]);
    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("12", oldLeg1);
    setup.routeLayer.expectRouteLegExists("23", oldLeg2);

    expect(setup.context.plan.source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sink.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).source.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(1).sink.nodeId).toEqual("1003");

  });

});
