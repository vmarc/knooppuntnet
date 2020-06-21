import {List} from "immutable";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";
import {PlannerCommandAddLeg} from "./planner-command-add-leg";
import {PlannerCommandAddStartPoint} from "./planner-command-add-start-point";
import {PlannerCommandReset} from "./planner-command-reset";

describe("PlannerCommandReset", () => {

  it("do and undo", () => {

    const setup = new PlannerTestSetup();

    const node1 = PlanNode.withCoordinate("1001", "01", [1, 1]);
    const node2 = PlanNode.withCoordinate("1002", "02", [2, 2]);
    const node3 = PlanNode.withCoordinate("1003", "03", [3, 3]);
    const leg1 = new PlanLeg("12", node1, node2, null, 0, List());
    const leg2 = new PlanLeg("23", node2, node3, null, 0, List());

    setup.legs.add(leg1);
    setup.legs.add(leg2);

    setup.context.execute(new PlannerCommandAddStartPoint(node1));
    setup.context.execute(new PlannerCommandAddLeg(leg1.featureId));
    setup.context.execute(new PlannerCommandAddLeg(leg2.featureId));

    setup.routeLayer.expectFlagCount(3);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, node1.featureId, [1, 1]);
    setup.routeLayer.expectFlagExists(PlanFlagType.Via, node2.featureId, [2, 2]);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, node3.featureId, [3, 3]);
    setup.routeLayer.expectRouteLegExists("12", leg1);
    setup.routeLayer.expectRouteLegExists("23", leg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sink.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(0).viaRoute).toEqual(null);
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).source.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(1).sink.nodeId).toEqual("1003");
    expect(setup.context.plan.legs.get(1).viaRoute).toEqual(null);

    const resetCommand = new PlannerCommandReset();
    setup.context.execute(resetCommand);

    expect(setup.context.plan.source).toEqual(null);
    expect(setup.context.plan.legs.size).toEqual(0);

    resetCommand.undo(setup.context);

    setup.routeLayer.expectFlagCount(3);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, node1.featureId, [1, 1]);
    setup.routeLayer.expectFlagExists(PlanFlagType.Via, node2.featureId, [2, 2]);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, node3.featureId, [3, 3]);
    setup.routeLayer.expectRouteLegExists("12", leg1);
    setup.routeLayer.expectRouteLegExists("23", leg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sink.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(0).viaRoute).toEqual(null);
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).source.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(1).sink.nodeId).toEqual("1003");
    expect(setup.context.plan.legs.get(1).viaRoute).toEqual(null);
  });

});
