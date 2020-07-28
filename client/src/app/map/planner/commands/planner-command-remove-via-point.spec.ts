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

    setup.context.execute(new PlannerCommandAddStartPoint(node1, startFlag));
    setup.context.execute(new PlannerCommandAddLeg(oldLeg1));
    setup.context.execute(new PlannerCommandAddLeg(oldLeg2));

    setup.markerLayer.expectFlagCount(3);
    setup.markerLayer.expectStartFlagExists("startFlag", [1, 1]);
    setup.markerLayer.expectViaFlagExists("oldSinkFlag1", [2, 2]);
    setup.markerLayer.expectEndFlagExists("oldSinkFlag2", [3, 3]);
    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("12", oldLeg1);
    setup.routeLayer.expectRouteLegExists("23", oldLeg2);

    {
      const legs = setup.context.plan.legs;
      expect(legs.size).toEqual(2);

      const leg1 = legs.get(0);
      expect(leg1.featureId).toEqual("12");
      expect(leg1.sourceNode.nodeId).toEqual("1001");
      expect(leg1.sinkNode.nodeId).toEqual("1002");
      TestSupport.expectViaFlag(leg1.sinkFlag, "oldSinkFlag1", [2, 2]);

      const leg2 = legs.get(1);
      expect(leg2.featureId).toEqual("23");
      expect(leg2.sourceNode.nodeId).toEqual("1002");
      expect(leg2.sinkNode.nodeId).toEqual("1003");
      TestSupport.expectEndFlag(leg2.sinkFlag, "oldSinkFlag2", [3, 3]);
    }

    const command = new PlannerCommandRemoveViaPoint(oldLeg1, oldLeg2, newLeg);
    setup.context.execute(command);

    setup.markerLayer.expectFlagCount(2);
    setup.markerLayer.expectStartFlagExists("startFlag", [1, 1]);
    setup.markerLayer.expectEndFlagExists("newSinkFlag", [3, 3]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("13", newLeg);

    {
      const legs = setup.context.plan.legs;
      expect(legs.size).toEqual(1);

      const leg = legs.get(0);
      expect(leg.featureId).toEqual("13");
      expect(leg.sourceNode.nodeId).toEqual("1001");
      expect(leg.sinkNode.nodeId).toEqual("1003");
      TestSupport.expectEndFlag(leg.sinkFlag, "newSinkFlag", [3, 3]);
    }

    command.undo(setup.context);

    setup.markerLayer.expectFlagCount(3);
    setup.markerLayer.expectStartFlagExists("startFlag", [1, 1]);
    setup.markerLayer.expectViaFlagExists("oldSinkFlag1", [2, 2]);
    setup.markerLayer.expectEndFlagExists("oldSinkFlag2", [3, 3]);
    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("12", oldLeg1);
    setup.routeLayer.expectRouteLegExists("23", oldLeg2);

    {
      const legs = setup.context.plan.legs;
      expect(legs.size).toEqual(2);

      const leg1 = legs.get(0);
      expect(leg1.featureId).toEqual("12");
      expect(leg1.sourceNode.nodeId).toEqual("1001");
      expect(leg1.sinkNode.nodeId).toEqual("1002");
      TestSupport.expectViaFlag(leg1.sinkFlag, "oldSinkFlag1", [2, 2]);

      const leg2 = legs.get(1);
      expect(leg2.featureId).toEqual("23");
      expect(leg2.sourceNode.nodeId).toEqual("1002");
      expect(leg2.sinkNode.nodeId).toEqual("1003");
      TestSupport.expectEndFlag(leg2.sinkFlag, "oldSinkFlag2", [3, 3]);
    }
  });

  it("do and undo - via route", () => {

    const setup = new PlannerTestSetup();

    const node1 = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);
    const node2 = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 2]);
    const node3 = PlanUtil.planNodeWithCoordinate("1003", "03", [3, 3]);

    const startFlag = PlanFlag.start("startFlag", [1, 1]);
    const oldViaFlag = PlanFlag.via("oldViaFlag", [15, 15]);
    const oldSinkFlag1 = PlanFlag.invisible("oldSinkFlag1", [2, 2]);
    const oldSinkFlag2 = PlanFlag.end("oldSinkFlag2", [3, 3]);
    const newSinkFlag = PlanFlag.end("newSinkFlag", [3, 3]);

    const oldLeg1 = PlanUtil.singleRoutePlanLeg("12", node1, node2, oldSinkFlag1, oldViaFlag);
    const oldLeg2 = PlanUtil.singleRoutePlanLeg("23", node2, node3, oldSinkFlag2, null);
    const newLeg = PlanUtil.singleRoutePlanLeg("13", node1, node3, newSinkFlag, null);

    setup.context.execute(new PlannerCommandAddStartPoint(node1, startFlag));
    setup.context.execute(new PlannerCommandAddLeg(oldLeg1));
    setup.context.execute(new PlannerCommandAddLeg(oldLeg2));

    setup.markerLayer.expectFlagCount(4);
    setup.markerLayer.expectStartFlagExists("startFlag", [1, 1]);
    setup.markerLayer.expectViaFlagExists("oldViaFlag", [15, 15]);
    setup.markerLayer.expectInvisibleFlagExists("oldSinkFlag1", [2, 2]);
    setup.markerLayer.expectEndFlagExists("oldSinkFlag2", [3, 3]);
    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("12", oldLeg1);
    setup.routeLayer.expectRouteLegExists("23", oldLeg2);

    {
      const legs = setup.context.plan.legs;
      expect(legs.size).toEqual(2);

      const leg1 = legs.get(0);
      expect(leg1.featureId).toEqual("12");
      expect(leg1.sourceNode.nodeId).toEqual("1001");
      expect(leg1.sinkNode.nodeId).toEqual("1002");
      TestSupport.expectViaFlag(leg1.viaFlag, "oldViaFlag", [15, 15]);
      TestSupport.expectInvisibleFlag(leg1.sinkFlag, "oldSinkFlag1", [2, 2]);

      const leg2 = legs.get(1);
      expect(leg2.featureId).toEqual("23");
      expect(leg2.sourceNode.nodeId).toEqual("1002");
      expect(leg2.sinkNode.nodeId).toEqual("1003");
      TestSupport.expectEndFlag(leg2.sinkFlag, "oldSinkFlag2", [3, 3]);
    }

    const command = new PlannerCommandRemoveViaPoint(oldLeg1, oldLeg2, newLeg);
    setup.context.execute(command);

    setup.markerLayer.expectFlagCount(2);
    setup.markerLayer.expectStartFlagExists("startFlag", [1, 1]);
    setup.markerLayer.expectEndFlagExists("newSinkFlag", [3, 3]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists("13", newLeg);

    {
      const legs = setup.context.plan.legs;
      expect(legs.size).toEqual(1);

      const leg = legs.get(0);
      expect(leg.featureId).toEqual("13");
      expect(leg.sourceNode.nodeId).toEqual("1001");
      expect(leg.sinkNode.nodeId).toEqual("1003");
      TestSupport.expectEndFlag(leg.sinkFlag, "newSinkFlag", [3, 3]);
    }

    command.undo(setup.context);

    setup.markerLayer.expectFlagCount(4);
    setup.markerLayer.expectStartFlagExists("startFlag", [1, 1]);
    setup.markerLayer.expectViaFlagExists("oldViaFlag", [15, 15]);
    setup.markerLayer.expectInvisibleFlagExists("oldSinkFlag1", [2, 2]);
    setup.markerLayer.expectEndFlagExists("oldSinkFlag2", [3, 3]);
    setup.routeLayer.expectRouteLegCount(2);
    setup.routeLayer.expectRouteLegExists("12", oldLeg1);
    setup.routeLayer.expectRouteLegExists("23", oldLeg2);

    {
      const legs = setup.context.plan.legs;
      expect(legs.size).toEqual(2);

      const leg1 = legs.get(0);
      expect(leg1.featureId).toEqual("12");
      expect(leg1.sourceNode.nodeId).toEqual("1001");
      expect(leg1.sinkNode.nodeId).toEqual("1002");
      TestSupport.expectViaFlag(leg1.viaFlag, "oldViaFlag", [15, 15]);
      TestSupport.expectInvisibleFlag(leg1.sinkFlag, "oldSinkFlag1", [2, 2]);

      const leg2 = legs.get(1);
      expect(leg2.featureId).toEqual("23");
      expect(leg2.sourceNode.nodeId).toEqual("1002");
      expect(leg2.sinkNode.nodeId).toEqual("1003");
      TestSupport.expectEndFlag(leg2.sinkFlag, "oldSinkFlag2", [3, 3]);
    }
  });

});
