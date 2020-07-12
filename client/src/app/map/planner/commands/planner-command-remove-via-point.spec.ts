import {TestSupport} from "../../../util/test-support";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlanFlag} from "../plan/plan-flag";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommandAddLeg} from "./planner-command-add-leg";
import {PlannerCommandAddStartPoint} from "./planner-command-add-start-point";
import {PlannerCommandRemoveViaPoint} from "./planner-command-remove-via-point";

describe("PlannerCommandRemoveViaPoint", () => {

  it("do and undo", () => {

    const setup = new PlannerTestSetup();

    const node1 = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);
    const node2 = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 2]);
    const node3 = PlanUtil.planNodeWithCoordinate("1003", "03", [3, 3]);

    const startFlag = PlanFlag.start("startFlag", [1, 1]);
    const oldSinkFlag1 = PlanFlag.via("oldSinkFlag1", [2, 2]);
    const oldSinkFlag2 = PlanFlag.end("oldSinkFlag2", [3, 3]);
    const newSinkFlag = PlanFlag.end("newSinkFlag", [3, 3]);

    const oldLeg1 = PlanUtil.singleRoutePlanLeg("12", node1, node2, oldSinkFlag1, null);
    const oldLeg2 = PlanUtil.singleRoutePlanLeg("23", node2, node3, oldSinkFlag2, null);
    const newLeg = PlanUtil.singleRoutePlanLeg("13", node1, node3, newSinkFlag, null);

    setup.legs.add(oldLeg1);
    setup.legs.add(oldLeg2);
    setup.legs.add(newLeg);

    setup.context.execute(new PlannerCommandAddStartPoint(node1, startFlag));
    setup.context.execute(new PlannerCommandAddLeg(oldLeg1.featureId));
    setup.context.execute(new PlannerCommandAddLeg(oldLeg2.featureId));

    setup.routeLayer.expectFlagCount(3);
    setup.routeLayer.expectStartFlagExists("startFlag", [1, 1]);
    setup.routeLayer.expectViaFlagExists("oldSinkFlag1", [2, 2]);
    setup.routeLayer.expectEndFlagExists("oldSinkFlag2", [3, 3]);
    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("12", oldLeg1);
    setup.routeLayer.expectRouteLegExists("23", oldLeg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sinkNode.nodeId).toEqual("1002");
    TestSupport.expectViaFlag(setup.context.plan.legs.get(0).sinkFlag, "oldSinkFlag1", [2, 2]);
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).sourceNode.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(1).sinkNode.nodeId).toEqual("1003");
    TestSupport.expectEndFlag(setup.context.plan.legs.get(1).sinkFlag, "oldSinkFlag2", [3, 3]);

    const command = new PlannerCommandRemoveViaPoint(oldLeg1.featureId, oldLeg2.featureId, newLeg.featureId);
    setup.context.execute(command);

    setup.routeLayer.expectFlagCount(2);
    setup.routeLayer.expectStartFlagExists("startFlag", [1, 1]);
    setup.routeLayer.expectEndFlagExists("newSinkFlag", [3, 3]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("13", newLeg);

    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("13");
    expect(setup.context.plan.legs.get(0).sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sinkNode.nodeId).toEqual("1003");
    TestSupport.expectEndFlag(setup.context.plan.legs.get(0).sinkFlag, "newSinkFlag", [3, 3]);

    command.undo(setup.context);

    setup.routeLayer.expectFlagCount(3);
    setup.routeLayer.expectStartFlagExists("startFlag", [1, 1]);
    setup.routeLayer.expectViaFlagExists("oldSinkFlag1", [2, 2]);
    setup.routeLayer.expectEndFlagExists("oldSinkFlag2", [3, 3]);
    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("12", oldLeg1);
    setup.routeLayer.expectRouteLegExists("23", oldLeg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sinkNode.nodeId).toEqual("1002");
    TestSupport.expectViaFlag(setup.context.plan.legs.get(0).sinkFlag, "oldSinkFlag1", [2, 2]);
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).sourceNode.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(1).sinkNode.nodeId).toEqual("1003");
    TestSupport.expectEndFlag(setup.context.plan.legs.get(1).sinkFlag, "oldSinkFlag2", [3, 3]);
  });

  it("do and undo - via route", () => {

    const setup = new PlannerTestSetup();

    const node1 = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);
    const node2 = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 2]);
    const node3 = PlanUtil.planNodeWithCoordinate("1003", "03", [3, 3]);

    const startFlag = PlanFlag.start("startFlag", [1, 1]);
    const oldViaFlag = PlanFlag.via("oldViaFlag", [15, 15]);
    const oldSinkFlag1 = PlanFlag.via("oldSinkFlag1", [2, 2]);
    const oldSinkFlag2 = PlanFlag.end("oldSinkFlag2", [3, 3]);
    const newSinkFlag = PlanFlag.end("newSinkFlag", [3, 3]);

    const oldLeg1 = PlanUtil.singleRoutePlanLeg("12", node1, node2, oldSinkFlag1, oldViaFlag);
    const oldLeg2 = PlanUtil.singleRoutePlanLeg("23", node2, node3, oldSinkFlag2, null);
    const newLeg = PlanUtil.singleRoutePlanLeg("13", node1, node3, newSinkFlag, null);

    setup.legs.add(oldLeg1);
    setup.legs.add(oldLeg2);
    setup.legs.add(newLeg);

    setup.context.execute(new PlannerCommandAddStartPoint(node1, startFlag));
    setup.context.execute(new PlannerCommandAddLeg(oldLeg1.featureId));
    setup.context.execute(new PlannerCommandAddLeg(oldLeg2.featureId));

    setup.routeLayer.expectFlagCount(4);
    setup.routeLayer.expectStartFlagExists("startFlag", [1, 1]);
    setup.routeLayer.expectViaFlagExists("oldViaFlag", [15, 15]);
    setup.routeLayer.expectInvisibleFlagExists("oldSinkFlag1", [2, 2]);
    setup.routeLayer.expectEndFlagExists("oldSinkFlag2", [3, 3]);
    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("12", oldLeg1);
    setup.routeLayer.expectRouteLegExists("23", oldLeg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sinkNode.nodeId).toEqual("1002");
    TestSupport.expectViaFlag(setup.context.plan.legs.get(0).viaFlag, "oldViaFlag", [15, 15]);
    TestSupport.expectInvisibleFlag(setup.context.plan.legs.get(0).sinkFlag, "oldSinkFlag1", [2, 2]);
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).sourceNode.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(1).sinkNode.nodeId).toEqual("1003");
    TestSupport.expectEndFlag(setup.context.plan.legs.get(1).sinkFlag, "oldSinkFlag2", [3, 3]);

    const command = new PlannerCommandRemoveViaPoint(oldLeg1.featureId, oldLeg2.featureId, newLeg.featureId);
    setup.context.execute(command);

    setup.routeLayer.expectFlagCount(2);
    setup.routeLayer.expectStartFlagExists("startFlag", [1, 1]);
    setup.routeLayer.expectEndFlagExists("newSinkFlag", [3, 3]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("13", newLeg);

    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("13");
    expect(setup.context.plan.legs.get(0).sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sinkNode.nodeId).toEqual("1003");
    TestSupport.expectEndFlag(setup.context.plan.legs.get(0).sinkFlag, "newSinkFlag", [3, 3]);

    command.undo(setup.context);

    setup.routeLayer.expectFlagCount(4);
    setup.routeLayer.expectStartFlagExists("startFlag", [1, 1]);
    setup.routeLayer.expectViaFlagExists("oldViaFlag", [15, 15]);
    setup.routeLayer.expectInvisibleFlagExists("oldSinkFlag1", [2, 2]);
    setup.routeLayer.expectEndFlagExists("oldSinkFlag2", [3, 3]);
    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("12", oldLeg1);
    setup.routeLayer.expectRouteLegExists("23", oldLeg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual("12");
    expect(setup.context.plan.legs.get(0).sourceNode.nodeId).toEqual("1001");
    expect(setup.context.plan.legs.get(0).sinkNode.nodeId).toEqual("1002");
    TestSupport.expectViaFlag(setup.context.plan.legs.get(0).viaFlag, "oldViaFlag", [15, 15]);
    TestSupport.expectInvisibleFlag(setup.context.plan.legs.get(0).sinkFlag, "oldSinkFlag1", [2, 2]);
    expect(setup.context.plan.legs.get(1).featureId).toEqual("23");
    expect(setup.context.plan.legs.get(1).sourceNode.nodeId).toEqual("1002");
    expect(setup.context.plan.legs.get(1).sinkNode.nodeId).toEqual("1003");
    TestSupport.expectEndFlag(setup.context.plan.legs.get(1).sinkFlag, "oldSinkFlag2", [3, 3]);
  });

});
