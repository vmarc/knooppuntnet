import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlanFlag} from "../plan/plan-flag";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommandAddLeg} from "./planner-command-add-leg";
import {PlannerCommandAddStartPoint} from "./planner-command-add-start-point";
import {PlannerCommandMoveViaPointToViaRoute} from "./planner-command-move-via-point-to-via-route";

describe("PlannerCommandMoveViaPointToViaRoute", () => {

  // TODO PLAN use LegEndRoute parameter instead

  it("do and undo", () => {

    const setup = new PlannerTestSetup();

    const sourceFlag = PlanFlag.start("sourceFlag", [1, 1]);
    const oldSinkFlag1 = PlanFlag.via("oldSinkFlag1", [2, 2]);
    const oldSinkFlag2 = PlanFlag.via("oldSinkFlag2", [3, 3]);
    const newSinkFlag = PlanFlag.via("newSinkFlag", [3, 3]);

    const oldLeg1 = PlanUtil.singleRoutePlanLeg("12", setup.node1, setup.node2, oldSinkFlag1, null);
    const oldLeg2 = PlanUtil.singleRoutePlanLeg("23", setup.node1, setup.node2, oldSinkFlag2, null);
    const newLeg = PlanUtil.singleRoutePlanLeg("13", setup.node1, setup.node2, newSinkFlag, null);

    setup.legs.add(oldLeg1);
    setup.legs.add(oldLeg2);
    setup.legs.add(newLeg);

    setup.context.execute(new PlannerCommandAddStartPoint(setup.node1, sourceFlag));
    setup.context.execute(new PlannerCommandAddLeg(oldLeg1.featureId));
    setup.context.execute(new PlannerCommandAddLeg(oldLeg2.featureId));

    setup.routeLayer.expectFlagCount(3);
    setup.routeLayer.expectStartFlagExists("sourceFlag", [1, 1]);
    setup.routeLayer.expectViaFlagExists("oldSinkFlag1", [2, 2]);
    setup.routeLayer.expectEndFlagExists("oldSinkFlag2", [3, 3]);
    setup.routeLayer.expectRouteLegExists("12", oldLeg1);
    setup.routeLayer.expectRouteLegExists("23", oldLeg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sinkNode.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).sourceNode.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(1).sinkNode.nodeId).toEqual("1003");

    const command = new PlannerCommandMoveViaPointToViaRoute(
      oldLeg1.featureId,
      oldLeg2.featureId,
      newLeg.featureId,
      // new ViaRoute(10, 1),
      [5, 5]
    );
    setup.context.execute(command);

    setup.routeLayer.expectFlagCount(3);
    setup.routeLayer.expectStartFlagExists("sourceFlag", [1, 1]);
    setup.routeLayer.expectViaFlagExists("10-1", [5, 5]);
    setup.routeLayer.expectEndFlagExists("TODO", [3, 3]);
    setup.routeLayer.expectRouteLegExists("13", newLeg);

    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("13");
    expect(setup.context.plan.legs.get(0).sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sinkNode.nodeId).toEqual("1003");

    command.undo(setup.context);

    setup.routeLayer.expectFlagCount(3);
    setup.routeLayer.expectStartFlagExists("sourceFlag", [1, 1]);
    setup.routeLayer.expectViaFlagExists("oldSinkFlag1", [2, 2]);
    setup.routeLayer.expectEndFlagExists("oldSinkFlag2", [3, 3]);
    setup.routeLayer.expectRouteLegExists("12", oldLeg1);
    setup.routeLayer.expectRouteLegExists("23", oldLeg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sinkNode.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).sourceNode.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(1).sinkNode.nodeId).toEqual("1003");
  });

});
