import {List} from "immutable";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";
import {PlannerCommandAddLeg} from "./planner-command-add-leg";
import {PlannerCommandAddStartPoint} from "./planner-command-add-start-point";
import {PlannerCommandRemoveViaPoint} from "./planner-command-remove-via-point";

describe("PlannerCommandRemoveViaPoint", () => {

  it("do and undo", () => {

    const setup = new PlannerTestSetup();

    const node1 = PlanNode.withCoordinate("1001", "01", [1, 1]);
    const node2 = PlanNode.withCoordinate("1002", "02", [2, 2]);
    const node3 = PlanNode.withCoordinate("1003", "03", [3, 3]);
    const oldLeg1 = new PlanLeg("", "12", node1, node2, 0, List());
    const oldLeg2 = new PlanLeg("", "23", node2, node3, 0, List());
    const newLeg = new PlanLeg("", "13", node1, node3, 0, List());

    setup.legs.add(oldLeg1);
    setup.legs.add(oldLeg2);
    setup.legs.add(newLeg);

    setup.context.execute(new PlannerCommandAddStartPoint(node1));
    setup.context.execute(new PlannerCommandAddLeg(oldLeg1.featureId));
    setup.context.execute(new PlannerCommandAddLeg(oldLeg2.featureId));

    setup.routeLayer.expectFlagCount(3);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, node1.featureId, [1, 1]);
    setup.routeLayer.expectFlagExists(PlanFlagType.Via, node2.featureId, [2, 2]);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, node3.featureId, [3, 3]);
    setup.routeLayer.expectRouteLegExists("12", oldLeg1);
    setup.routeLayer.expectRouteLegExists("23", oldLeg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sink.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).source.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(1).sink.nodeId).toEqual("1003");

    const command = new PlannerCommandRemoveViaPoint(oldLeg1.featureId, oldLeg2.featureId, newLeg.featureId);
    setup.context.execute(command);

    setup.routeLayer.expectFlagCount(2);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, node1.featureId, [1, 1]);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, node3.featureId, [3, 3]);
    setup.routeLayer.expectRouteLegExists("13", newLeg);

    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("13");
    expect(setup.context.plan.legs.get(0).source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sink.nodeId).toEqual("1003");

    command.undo(setup.context);

    setup.routeLayer.expectFlagCount(3);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, node1.featureId, [1, 1]);
    setup.routeLayer.expectFlagExists(PlanFlagType.Via, node2.featureId, [2, 2]);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, node3.featureId, [3, 3]);
    setup.routeLayer.expectRouteLegExists("12", oldLeg1);
    setup.routeLayer.expectRouteLegExists("23", oldLeg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sink.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).source.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(1).sink.nodeId).toEqual("1003");
  });

});
