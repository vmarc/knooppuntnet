import {PlanReverser} from "./plan-reverser";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlanUtil} from "./plan-util";
import {List} from "immutable";
import {PlanLegDetail} from "../../../kpn/api/common/planner/plan-leg-detail";
import {PlanRoute} from "../../../kpn/api/common/planner/plan-route";
import {Plan} from "./plan";
import {PlanFlag} from "./plan-flag";
import {PlanFragment} from "../../../kpn/api/common/planner/plan-fragment";
import {PlanSegment} from "../../../kpn/api/common/planner/plan-segment";
import {PlanLeg} from "./plan-leg";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";
import {LegEndRoute} from "../../../kpn/api/common/planner/leg-end-route";
import {TrackPathKey} from "../../../kpn/api/common/common/track-path-key";
import {TestSupport} from "../../../util/test-support";

describe("PlanReverser", () => {

  it("reverse empty plan", () => {

    const setup = new PlannerTestSetup();
    const oldPlan = Plan.empty;

    new PlanReverser(setup.context).reverse(oldPlan).subscribe(newPlan => {
      expect(newPlan.sourceNode).toEqual(null);
      expect(newPlan.sourceFlag).toEqual(null);
      expect(newPlan.legs.isEmpty()).toEqual(true);
    });
  });

  it("reverse plan with start node only", () => {

    const setup = new PlannerTestSetup();
    const oldPlan = setup.createPlanWithStartPointOnly();

    new PlanReverser(setup.context).reverse(oldPlan).subscribe(newPlan => {
      expect(newPlan.sourceNode.nodeId).toEqual("1001");
      TestSupport.expectStartFlagCoordinate(newPlan.sourceFlag, [1, 1]);
      expect(newPlan.legs.isEmpty()).toEqual(true);
    });
  });

  it("reverse plan with single node-to-node leg", () => {

    const setup = new PlannerTestSetup();

    const sourceNode = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 2]);
    const sinkNode = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);

    const planRoute = new PlanRoute(sourceNode, sinkNode, 0, List(), List());
    const planLegDetail = new PlanLegDetail(List([planRoute]));

    setup.legRepository.add(PlanUtil.legEndNode(1002), PlanUtil.legEndNode(1001), planLegDetail);

    const oldPlan = setup.createOneLegPlan();

    new PlanReverser(setup.context).reverse(oldPlan).subscribe(newPlan => {

      expect(newPlan.sourceNode.nodeId).toEqual("1002");
      TestSupport.expectStartFlagCoordinate(newPlan.sourceFlag, [2, 2]);

      expect(newPlan.legs.size).toEqual(1);
      const leg = newPlan.legs.get(0);
      expect(leg.source.node.nodeId).toEqual(1002);
      expect(leg.sink.node.nodeId).toEqual(1001);
      TestSupport.expectEndFlagCoordinate(leg.sinkFlag, [1, 1]);
      expect(leg.viaFlag).toEqual(null);
    });
  });

  it("reverse plan with multiple node-to-node legs", () => {

    const setup = new PlannerTestSetup();

    const planNode1 = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);
    const planNode2 = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 2]);
    const planNode3 = PlanUtil.planNodeWithCoordinate("1003", "03", [3, 3]);
    const planNode4 = PlanUtil.planNodeWithCoordinate("1004", "04", [4, 4]);

    const planRoute43 = new PlanRoute(planNode4, planNode3, 0, List(), List());
    const planRoute32 = new PlanRoute(planNode3, planNode2, 0, List(), List());
    const planRoute21 = new PlanRoute(planNode2, planNode1, 0, List(), List());

    const planLegDetail43 = new PlanLegDetail(List([planRoute43]));
    const planLegDetail32 = new PlanLegDetail(List([planRoute32]));
    const planLegDetail21 = new PlanLegDetail(List([planRoute21]));

    setup.legRepository.add(PlanUtil.legEndNode(1004), PlanUtil.legEndNode(1003), planLegDetail43);
    setup.legRepository.add(PlanUtil.legEndNode(1003), PlanUtil.legEndNode(1002), planLegDetail32);
    setup.legRepository.add(PlanUtil.legEndNode(1002), PlanUtil.legEndNode(1001), planLegDetail21);

    const oldPlan = setup.createThreeLegPlan();

    new PlanReverser(setup.context).reverse(oldPlan).subscribe(newPlan => {

      expect(newPlan.sourceNode.nodeId).toEqual("1004");
      TestSupport.expectStartFlagCoordinate(newPlan.sourceFlag, [4, 4]);

      expect(newPlan.legs.size).toEqual(3);

      const leg1 = newPlan.legs.get(0);
      expect(leg1.source.node.nodeId).toEqual(1004);
      expect(leg1.sink.node.nodeId).toEqual(1003);
      TestSupport.expectViaFlagCoordinate(leg1.sinkFlag, [3, 3]);
      expect(leg1.viaFlag).toEqual(null);

      const leg2 = newPlan.legs.get(1);
      expect(leg2.source.node.nodeId).toEqual(1003);
      expect(leg2.sink.node.nodeId).toEqual(1002);
      TestSupport.expectViaFlagCoordinate(leg2.sinkFlag, [2, 2]);
      expect(leg2.viaFlag).toEqual(null);

      const leg3 = newPlan.legs.get(2);
      expect(leg3.source.node.nodeId).toEqual(1002);
      expect(leg3.sink.node.nodeId).toEqual(1001);
      TestSupport.expectEndFlagCoordinate(leg3.sinkFlag, [1, 1]);
      expect(leg3.viaFlag).toEqual(null);

    });
  });

  it("reverse plan with single via-route leg", () => {

    const setup = new PlannerTestSetup();

    buildExpectedSingleViaRouteLeg(setup);
    const oldPlan = buildSingleViaRoutePlan(setup);

    new PlanReverser(setup.context).reverse(oldPlan).subscribe(newPlan => {

      expect(newPlan.sourceNode.nodeId).toEqual("1003");
      TestSupport.expectStartFlagCoordinate(newPlan.sourceFlag, [3, 3]);

      expect(newPlan.legs.size).toEqual(1);
      const leg = newPlan.legs.get(0);
      expect(leg.source.node.nodeId).toEqual(1003);
      expect(leg.sink.route.trackPathKeys.size).toEqual(1);
      expect(leg.sink.route.trackPathKeys.get(0).routeId).toEqual(10);
      expect(leg.sink.route.trackPathKeys.get(0).pathId).toEqual(1);
      TestSupport.expectViaFlagCoordinate(leg.viaFlag, [2, 2]);
      TestSupport.expectEndFlagCoordinate(leg.sinkFlag, [1, 1]);
    });
  });

  function buildExpectedSingleViaRouteLeg(setup: PlannerTestSetup): void {

    const sourceNode = PlanUtil.planNodeWithCoordinate("1003", "03", [3, 3]);
    const sinkNode = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);

    const planRoute = new PlanRoute(sourceNode, sinkNode, 0, List(), List());
    const planLegDetail = new PlanLegDetail(List([planRoute]));

    const trackPathKey = new TrackPathKey(10, 1);
    const source = PlanUtil.legEndNode(1003);
    const sink = new LegEnd(null, new LegEndRoute(List([trackPathKey])));

    setup.legRepository.add(source, sink, planLegDetail);
  }

  function buildSingleViaRoutePlan(setup: PlannerTestSetup): Plan {

    const sourceNode = setup.node1;
    const viaNode = setup.node2;
    const sinkNode = setup.node3;

    const sourceFlag = PlanFlag.start("sourceFlag", sourceNode.coordinate);
    const viaFlag = PlanFlag.via("viaFlag", viaNode.coordinate);
    const sinkFlag = PlanFlag.end("sinkFlag", sinkNode.coordinate);
    const trackPathKey = new TrackPathKey(10, 1);

    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = new LegEnd(null, new LegEndRoute(List([trackPathKey])));
    const legKey = PlanUtil.key(source, sink);

    const fragment = new PlanFragment(0, 0, -1, sinkNode.coordinate, sinkNode.latLon);
    const segment = new PlanSegment(0, "", null, List([fragment]));
    const route = new PlanRoute(sourceNode, sinkNode, 0, List([segment]), List());
    const leg = new PlanLeg("11", legKey, source, sink, sinkFlag, viaFlag, List([route]));

    setup.context.legs.add(leg);

    const oldPlan = new Plan(sourceNode, sourceFlag, List([leg]));
    setup.context.updatePlan(oldPlan);
    setup.markerLayer.addFlag(sourceFlag);
    setup.markerLayer.addFlag(viaFlag);
    setup.markerLayer.addFlag(sinkFlag);
    setup.routeLayer.addPlanLeg(leg);

    return oldPlan;
  }

  it("reverse plan with node-to-node leg and via-route leg", () => {

    const setup = new PlannerTestSetup();
    buildExpectedViaRouteLeg(setup);
    buildExpectedNodeToNodeLeg(setup);
    const oldPlan = buildPlan(setup);

    new PlanReverser(setup.context).reverse(oldPlan).subscribe(newPlan => {

      expect(newPlan.sourceNode.nodeId).toEqual("1004");
      TestSupport.expectStartFlagCoordinate(newPlan.sourceFlag, [4, 4]);

      expect(newPlan.legs.size).toEqual(2);

      const leg1 = newPlan.legs.get(0);
      expect(leg1.source.node.nodeId).toEqual(1004);
      expect(leg1.sink.route.trackPathKeys.size).toEqual(1);
      expect(leg1.sink.route.trackPathKeys.get(0).routeId).toEqual(10);
      expect(leg1.sink.route.trackPathKeys.get(0).pathId).toEqual(1);
      TestSupport.expectViaFlagCoordinate(leg1.viaFlag, [3, 3]);
      TestSupport.expectViaFlagCoordinate(leg1.sinkFlag, [2, 2]);

      const leg2 = newPlan.legs.get(1);
      expect(leg2.source.node.nodeId).toEqual(1002);
      expect(leg2.sink.node.nodeId).toEqual(1001);
      expect(leg2.viaFlag).toEqual(null);
      TestSupport.expectEndFlagCoordinate(leg2.sinkFlag, [1, 1]);

    });
  });

  function buildExpectedViaRouteLeg(setup: PlannerTestSetup): void {

    const sourceNode = PlanUtil.planNodeWithCoordinate("1004", "04", [4, 4]);
    const sinkNode = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 2]);

    const planRoute = new PlanRoute(sourceNode, sinkNode, 0, List(), List());
    const planLegDetail = new PlanLegDetail(List([planRoute]));

    const trackPathKey = new TrackPathKey(10, 1);
    const source = PlanUtil.legEndNode(1004);
    const sink = new LegEnd(null, new LegEndRoute(List([trackPathKey])));

    setup.legRepository.add(source, sink, planLegDetail);
  }

  function buildExpectedNodeToNodeLeg(setup: PlannerTestSetup): void {

    const sourceNode = PlanUtil.planNodeWithCoordinate("1002", "02", [2, 2]);
    const sinkNode = PlanUtil.planNodeWithCoordinate("1001", "01", [1, 1]);

    const planRoute = new PlanRoute(sourceNode, sinkNode, 0, List(), List());
    const planLegDetail = new PlanLegDetail(List([planRoute]));

    const source = PlanUtil.legEndNode(1002);
    const sink = PlanUtil.legEndNode(1001);

    setup.legRepository.add(source, sink, planLegDetail);
  }

  function buildPlan(setup: PlannerTestSetup): Plan {

    const sourceNode = setup.node1;
    const viaNode = setup.node3;
    const sinkNode = setup.node4;

    const sourceFlag = PlanFlag.start("sourceFlag", sourceNode.coordinate);
    const viaFlag = PlanFlag.via("viaFlag", viaNode.coordinate);
    const sinkFlag = PlanFlag.end("sinkFlag", sinkNode.coordinate);
    const trackPathKey = new TrackPathKey(10, 1);

    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = new LegEnd(null, new LegEndRoute(List([trackPathKey])));
    const legKey = PlanUtil.key(source, sink);

    const fragment = new PlanFragment(0, 0, -1, sinkNode.coordinate, sinkNode.latLon);
    const segment = new PlanSegment(0, "", null, List([fragment]));
    const route = new PlanRoute(sourceNode, sinkNode, 0, List([segment]), List());
    const leg = new PlanLeg("11", legKey, source, sink, sinkFlag, viaFlag, List([route]));

    setup.context.legs.add(leg);

    const oldPlan = new Plan(sourceNode, sourceFlag, List([leg]));
    setup.context.updatePlan(oldPlan);
    setup.markerLayer.addFlag(sourceFlag);
    setup.markerLayer.addFlag(viaFlag);
    setup.markerLayer.addFlag(sinkFlag);
    setup.routeLayer.addPlanLeg(leg);

    return oldPlan;
  }

});
