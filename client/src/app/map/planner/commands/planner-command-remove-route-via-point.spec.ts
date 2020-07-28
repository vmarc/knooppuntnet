import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlanFlag} from "../plan/plan-flag";
import {PlanUtil} from "../plan/plan-util";
import {PlannerCommandAddLeg} from "./planner-command-add-leg";
import {PlannerCommandAddStartPoint} from "./planner-command-add-start-point";
import {PlannerCommandRemoveRouteViaPoint} from "./planner-command-remove-route-via-point";

describe("PlannerCommandRemoveRouteViaPoint", () => {

  it("do and undo", () => {

    const setup = new PlannerTestSetup();

    const sourceFlag = PlanFlag.start("sourceFlag", [1, 1]);
    const viaFlag = PlanFlag.via("viaFlag", [2, 2]);
    const sinkFlag = PlanFlag.end("sinkFlag", [3, 3]);

    const oldLeg = PlanUtil.singleRoutePlanLeg("13-1", setup.node1, setup.node3, sinkFlag, viaFlag);
    const newLeg = PlanUtil.singleRoutePlanLeg("13-2", setup.node1, setup.node3, sinkFlag, null);

    setup.context.execute(new PlannerCommandAddStartPoint(setup.node1, sourceFlag));
    setup.context.execute(new PlannerCommandAddLeg(oldLeg));

    setup.markerLayer.expectFlagCount(3);
    setup.markerLayer.expectStartFlagExists("sourceFlag", [1, 1]);
    setup.markerLayer.expectViaFlagExists("viaFlag", [2, 2]);
    setup.markerLayer.expectEndFlagExists("sinkFlag", [3, 3]);
    setup.routeLayer.expectRouteLegExists("13-1", oldLeg);

    {
      const legs = setup.context.plan.legs;
      expect(legs.size).toEqual(1);

      const leg = legs.get(0);
      expect(leg.featureId).toEqual("13-1");
      expect(leg.sourceNode.nodeId).toEqual("1001");
      expect(leg.sinkNode.nodeId).toEqual("1003");
    }

    const command = new PlannerCommandRemoveRouteViaPoint(oldLeg, newLeg);
    setup.context.execute(command);

    setup.markerLayer.expectFlagCount(2);
    setup.markerLayer.expectStartFlagExists("sourceFlag", [1, 1]);
    setup.markerLayer.expectEndFlagExists("sinkFlag", [3, 3]);
    setup.routeLayer.expectRouteLegExists("13-2", newLeg);

    {
      const legs = setup.context.plan.legs;
      expect(legs.size).toEqual(1);

      const leg = legs.get(0);
      expect(leg.featureId).toEqual("13-2");
      expect(leg.sourceNode.nodeId).toEqual("1001");
      expect(leg.sinkNode.nodeId).toEqual("1003");
    }

    command.undo(setup.context);

    setup.markerLayer.expectFlagCount(3);
    setup.markerLayer.expectStartFlagExists("sourceFlag", [1, 1]);
    setup.markerLayer.expectViaFlagExists("viaFlag", [2, 2]);
    setup.markerLayer.expectEndFlagExists("sinkFlag", [3, 3]);
    setup.routeLayer.expectRouteLegExists("13-1", oldLeg);

    {
      const legs = setup.context.plan.legs;
      expect(legs.size).toEqual(1);

      const leg = legs.get(0);
      expect(leg.featureId).toEqual("13-1");
      expect(leg.sourceNode.nodeId).toEqual("1001");
      expect(leg.sinkNode.nodeId).toEqual("1003");
    }
  });

});
