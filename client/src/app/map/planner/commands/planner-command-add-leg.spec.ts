import {List} from "immutable";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";
import {PlannerCommandAddLeg} from "./planner-command-add-leg";
import {PlannerCommandAddStartPoint} from "./planner-command-add-start-point";

describe("PlannerCommandAddLeg", () => {

  it("do and undo", () => {

    const setup = new PlannerTestSetup();

    const node1 = PlanNode.withCoordinate("1001", "01", [1, 1]);
    const node2 = PlanNode.withCoordinate("1002", "02", [2, 2]);
    const leg = new PlanLeg("12", "", node1, node2, 0, List());

    const addStartCommand = new PlannerCommandAddStartPoint(node1);
    setup.context.execute(addStartCommand);

    setup.legs.add(leg);

    const command = new PlannerCommandAddLeg(leg.featureId);
    setup.context.execute(command);

    setup.routeLayer.expectFlagCount(2);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, node1.featureId, [1, 1]);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, node2.featureId, [2, 2]);
    setup.routeLayer.expectRouteLegExists("12", leg);

    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).source.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sink.nodeId).toEqual("1002");

    command.undo(setup.context);

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectRouteLegCount(0);
    expect(setup.context.plan.legs.size).toEqual(0);
  });

  it("previous end flag changes to via flag", () => {

    const setup = new PlannerTestSetup();

    const node1 = PlanNode.withCoordinate("1001", "01", [1, 1]);
    const node2 = PlanNode.withCoordinate("1002", "02", [2, 2]);
    const node3 = PlanNode.withCoordinate("1003", "03", [3, 3]);
    const node4 = PlanNode.withCoordinate("1004", "04", [4, 4]);

    const leg1 = new PlanLeg("12", "", node1, node2, 0, List());
    const leg2 = new PlanLeg("23", "", node2, node3, 0, List());
    const leg3 = new PlanLeg("34", "", node3, node4, 0, List());

    const addStartCommand = new PlannerCommandAddStartPoint(node1);
    setup.context.execute(addStartCommand);

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, node1.featureId, [1, 1]);

    setup.legs.add(leg1);
    const addLeg1Command = new PlannerCommandAddLeg(leg1.featureId);
    setup.context.execute(addLeg1Command);
    setup.routeLayer.expectFlagCount(2);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, node1.featureId, [1, 1]);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, node2.featureId, [2, 2]);
    setup.routeLayer.expectRouteLegExists("12", leg1);

    setup.legs.add(leg2);
    const addLeg2Command = new PlannerCommandAddLeg(leg2.featureId);
    setup.context.execute(addLeg2Command);
    setup.routeLayer.expectFlagCount(3);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, node1.featureId, [1, 1]);
    setup.routeLayer.expectFlagExists(PlanFlagType.Via, node2.featureId, [2, 2]);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, node3.featureId, [3, 3]);
    setup.routeLayer.expectRouteLegExists("12", leg1);
    setup.routeLayer.expectRouteLegExists("23", leg2);

    setup.legs.add(leg3);
    const addLeg3Command = new PlannerCommandAddLeg(leg3.featureId);
    setup.context.execute(addLeg3Command);
    setup.routeLayer.expectFlagCount(4);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, node1.featureId, [1, 1]);
    setup.routeLayer.expectFlagExists(PlanFlagType.Via, node2.featureId, [2, 2]);
    setup.routeLayer.expectFlagExists(PlanFlagType.Via, node3.featureId, [3, 3]);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, node4.featureId, [4, 4]);
    setup.routeLayer.expectRouteLegCount(3);
    setup.routeLayer.expectRouteLegExists("12", leg1);
    setup.routeLayer.expectRouteLegExists("23", leg2);
    setup.routeLayer.expectRouteLegExists("34", leg3);

    addLeg3Command.undo(setup.context);
    setup.routeLayer.expectFlagCount(3);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, node1.featureId, [1, 1]);
    setup.routeLayer.expectFlagExists(PlanFlagType.Via, node2.featureId, [2, 2]);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, node3.featureId, [3, 3]);
    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("12", leg1);
    setup.routeLayer.expectRouteLegExists("23", leg2);

    addLeg2Command.undo(setup.context);
    setup.routeLayer.expectFlagCount(2);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, node1.featureId, [1, 1]);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, node2.featureId, [2, 2]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("12", leg1);


    addLeg1Command.undo(setup.context);
    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, node1.featureId, [1, 1]);
    setup.routeLayer.expectRouteLegCount(0);

    addStartCommand.undo(setup.context);
    setup.routeLayer.expectFlagCount(0);
    setup.routeLayer.expectRouteLegCount(0);
  });

});
