import {List} from "immutable";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlanFlag} from "../plan/plan-flag";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommandAddLeg} from "./planner-command-add-leg";
import {PlannerCommandAddStartPoint} from "./planner-command-add-start-point";

describe("PlannerCommandAddLeg", () => {

  it("do and undo", () => {

    const setup = new PlannerTestSetup();

    const node1 = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);
    const node2 = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 2]);

    const legEnd1 = PlanUtil.legEndNode(+node1.nodeId);
    const legEnd2 = PlanUtil.legEndNode(+node2.nodeId);

    const leg = new PlanLeg("12", "", legEnd1, legEnd2, PlanFlag.end("n2", node2), null, List());

    const addStartCommand = new PlannerCommandAddStartPoint("n1", node1);
    setup.context.execute(addStartCommand);

    setup.legs.add(leg);

    const command = new PlannerCommandAddLeg(leg.featureId);
    setup.context.execute(command);

    setup.routeLayer.expectFlagCount(2);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, "n1", [1, 1]);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, "n2", [2, 2]);
    setup.routeLayer.expectRouteLegExists("12", leg);

    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sinkFlag.featureId).toEqual("n2");
    expect(setup.context.plan.legs.get(0).sinkFlag.flagType).toEqual(PlanFlagType.End);
    expect(setup.context.plan.legs.get(0).viaFlag).toEqual(null);

    command.undo(setup.context);

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectRouteLegCount(0);
    expect(setup.context.plan.legs.size).toEqual(0);
  });

  it("previous end flag changes to via flag", () => {

    const setup = new PlannerTestSetup();

    const node1 = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);
    const node2 = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 2]);
    const node3 = PlanUtil.planNodeWithCoordinate("1003", "03", [3, 3]);
    const node4 = PlanUtil.planNodeWithCoordinate("1004", "04", [4, 4]);

    const legEnd1 = PlanUtil.legEndNode(+node1.nodeId);
    const legEnd2 = PlanUtil.legEndNode(+node2.nodeId);
    const legEnd3 = PlanUtil.legEndNode(+node3.nodeId);
    const legEnd4 = PlanUtil.legEndNode(+node4.nodeId);

    const leg1 = new PlanLeg("12", "", legEnd1, legEnd2, PlanFlag.end("n2", node2), null, List());
    const leg2 = new PlanLeg("23", "", legEnd2, legEnd3, PlanFlag.end("n3", node3), null, List());
    const leg3 = new PlanLeg("34", "", legEnd3, legEnd4, PlanFlag.end("n4", node4), null, List());

    const addStartCommand = new PlannerCommandAddStartPoint("n1", node1);
    setup.context.execute(addStartCommand);

    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, "n1", [1, 1]);

    setup.legs.add(leg1);
    const addLeg1Command = new PlannerCommandAddLeg(leg1.featureId);
    setup.context.execute(addLeg1Command);
    setup.routeLayer.expectFlagCount(2);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, "n1", [1, 1]);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, "n2", [2, 2]);
    setup.routeLayer.expectRouteLegExists("12", leg1);

    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sinkFlag.featureId).toEqual("n2");
    expect(setup.context.plan.legs.get(0).sinkFlag.flagType).toEqual(PlanFlagType.End);
    expect(setup.context.plan.legs.get(0).viaFlag).toEqual(null);

    setup.legs.add(leg2);
    const addLeg2Command = new PlannerCommandAddLeg(leg2.featureId);
    setup.context.execute(addLeg2Command);
    setup.routeLayer.expectFlagCount(3);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, "n1", [1, 1]);
    setup.routeLayer.expectFlagExists(PlanFlagType.Via, "n2", [2, 2]);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, "n3", [3, 3]);
    setup.routeLayer.expectRouteLegExists("12", leg1);
    setup.routeLayer.expectRouteLegExists("23", leg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sinkFlag.featureId).toEqual("n2");
    expect(setup.context.plan.legs.get(0).sinkFlag.flagType).toEqual(PlanFlagType.Via);
    expect(setup.context.plan.legs.get(0).viaFlag).toEqual(null);
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).sinkFlag.featureId).toEqual("n3");
    expect(setup.context.plan.legs.get(1).sinkFlag.flagType).toEqual(PlanFlagType.End);
    expect(setup.context.plan.legs.get(1).viaFlag).toEqual(null);

    setup.legs.add(leg3);
    const addLeg3Command = new PlannerCommandAddLeg(leg3.featureId);
    setup.context.execute(addLeg3Command);
    setup.routeLayer.expectFlagCount(4);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, "n1", [1, 1]);
    setup.routeLayer.expectFlagExists(PlanFlagType.Via, "n2", [2, 2]);
    setup.routeLayer.expectFlagExists(PlanFlagType.Via, "n3", [3, 3]);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, "n4", [4, 4]);
    setup.routeLayer.expectRouteLegCount(3);
    setup.routeLayer.expectRouteLegExists("12", leg1);
    setup.routeLayer.expectRouteLegExists("23", leg2);
    setup.routeLayer.expectRouteLegExists("34", leg3);

    expect(setup.context.plan.legs.size).toEqual(3);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sinkFlag.featureId).toEqual("n2");
    expect(setup.context.plan.legs.get(0).sinkFlag.flagType).toEqual(PlanFlagType.Via);
    expect(setup.context.plan.legs.get(0).viaFlag).toEqual(null);
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).sinkFlag.featureId).toEqual("n3");
    expect(setup.context.plan.legs.get(1).sinkFlag.flagType).toEqual(PlanFlagType.Via);
    expect(setup.context.plan.legs.get(1).viaFlag).toEqual(null);
    expect(setup.context.plan.legs.get(2).featureId).toEqual("34");
    expect(setup.context.plan.legs.get(2).sinkFlag.featureId).toEqual("n4");
    expect(setup.context.plan.legs.get(2).sinkFlag.flagType).toEqual(PlanFlagType.End);
    expect(setup.context.plan.legs.get(2).viaFlag).toEqual(null);

    addLeg3Command.undo(setup.context);
    setup.routeLayer.expectFlagCount(3);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, "n1", [1, 1]);
    setup.routeLayer.expectFlagExists(PlanFlagType.Via, "n2", [2, 2]);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, "n3", [3, 3]);
    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("12", leg1);
    setup.routeLayer.expectRouteLegExists("23", leg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sinkFlag.featureId).toEqual("n2");
    expect(setup.context.plan.legs.get(0).sinkFlag.flagType).toEqual(PlanFlagType.Via);
    expect(setup.context.plan.legs.get(0).viaFlag).toEqual(null);
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).sinkFlag.featureId).toEqual("n3");
    expect(setup.context.plan.legs.get(1).sinkFlag.flagType).toEqual(PlanFlagType.End);
    expect(setup.context.plan.legs.get(1).viaFlag).toEqual(null);

    addLeg2Command.undo(setup.context);
    setup.routeLayer.expectFlagCount(2);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, "n1", [1, 1]);
    setup.routeLayer.expectFlagExists(PlanFlagType.End, "n2", [2, 2]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("12", leg1);

    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sinkFlag.featureId).toEqual("n2");
    expect(setup.context.plan.legs.get(0).sinkFlag.flagType).toEqual(PlanFlagType.End);
    expect(setup.context.plan.legs.get(0).viaFlag).toEqual(null);

    addLeg1Command.undo(setup.context);
    setup.routeLayer.expectFlagCount(1);
    setup.routeLayer.expectFlagExists(PlanFlagType.Start, "n1", [1, 1]);
    setup.routeLayer.expectRouteLegCount(0);

    expect(setup.context.plan.legs.size).toEqual(0);

    addStartCommand.undo(setup.context);
    setup.routeLayer.expectFlagCount(0);
    setup.routeLayer.expectRouteLegCount(0);
  });

});
