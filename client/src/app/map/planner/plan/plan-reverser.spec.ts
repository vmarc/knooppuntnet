import { TrackPathKey } from '@api/common/common/track-path-key';
import { LegEnd } from '@api/common/planner/leg-end';
import { PlanFragment } from '@api/common/planner/plan-fragment';
import { PlanRoute } from '@api/common/planner/plan-route';
import { PlanSegment } from '@api/common/planner/plan-segment';
import { List } from 'immutable';
import { expectEndFlagCoordinate } from '../../../util/test-support';
import { expectStartFlagCoordinate } from '../../../util/test-support';
import { expectViaFlagCoordinate } from '../../../util/test-support';
import { expectInvisibleFlagCoordinate } from '../../../util/test-support';
import { PlanLegData } from '../context/plan-leg-data';
import { PlannerTestSetup } from '../context/planner-test-setup';
import { Plan } from './plan';
import { PlanFlag } from './plan-flag';
import { PlanLeg } from './plan-leg';
import { PlanReverser } from './plan-reverser';
import { PlanUtil } from './plan-util';

describe('PlanReverser', () => {
  it('reverse empty plan', () => {
    const setup = new PlannerTestSetup();
    const oldPlan = Plan.empty;

    new PlanReverser(setup.context).reverse(oldPlan).subscribe((newPlan) => {
      expect(newPlan.sourceNode).toEqual(null);
      expect(newPlan.sourceFlag).toEqual(null);
      expect(newPlan.legs.isEmpty()).toEqual(true);
    });
  });

  it('reverse plan with start node only', () => {
    const setup = new PlannerTestSetup();
    const oldPlan = setup.createPlanWithStartPointOnly();

    new PlanReverser(setup.context).reverse(oldPlan).subscribe((newPlan) => {
      expect(newPlan.sourceNode.nodeId).toEqual('1001');
      expectStartFlagCoordinate(newPlan.sourceFlag, [1, 1]);
      expect(newPlan.legs.isEmpty()).toEqual(true);
    });
  });

  it('reverse plan with single node-to-node leg', () => {
    const setup = new PlannerTestSetup();

    const sourceNode = PlanUtil.planNodeWithCoordinate('1002', '02', null, [
      2,
      2,
    ]);
    const sinkNode = PlanUtil.planNodeWithCoordinate('1001', '01', null, [
      1,
      1,
    ]);
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    const planRoute: PlanRoute = {
      sourceNode,
      sinkNode,
      meters: 0,
      segments: [],
      streets: [],
    };
    const planLegData = new PlanLegData(source, sink, List([planRoute]));

    setup.legRepository.add(planLegData);

    const oldPlan = setup.createOneLegPlan();

    new PlanReverser(setup.context).reverse(oldPlan).subscribe((newPlan) => {
      expect(newPlan.sourceNode.nodeId).toEqual('1002');
      expectStartFlagCoordinate(newPlan.sourceFlag, [2, 2]);

      expect(newPlan.legs.size).toEqual(1);
      const leg = newPlan.legs.get(0);
      expect(leg.source.node.nodeId).toEqual(1002);
      expect(leg.sink.node.nodeId).toEqual(1001);
      expectEndFlagCoordinate(leg.sinkFlag, [1, 1]);
      expect(leg.viaFlag).toEqual(null);
    });
  });

  it('reverse plan with multiple node-to-node legs', () => {
    const setup = new PlannerTestSetup();

    const planNode1 = PlanUtil.planNodeWithCoordinate('1001', '01', null, [
      1,
      1,
    ]);
    const planNode2 = PlanUtil.planNodeWithCoordinate('1002', '02', null, [
      2,
      2,
    ]);
    const planNode3 = PlanUtil.planNodeWithCoordinate('1003', '03', null, [
      3,
      3,
    ]);
    const planNode4 = PlanUtil.planNodeWithCoordinate('1004', '04', null, [
      4,
      4,
    ]);

    const legEnd1 = PlanUtil.legEndNode(+planNode1.nodeId);
    const legEnd2 = PlanUtil.legEndNode(+planNode2.nodeId);
    const legEnd3 = PlanUtil.legEndNode(+planNode3.nodeId);
    const legEnd4 = PlanUtil.legEndNode(+planNode4.nodeId);

    const planRoute43: PlanRoute = {
      sourceNode: planNode4,
      sinkNode: planNode3,
      meters: 0,
      segments: [],
      streets: [],
    };
    const planRoute32: PlanRoute = {
      sourceNode: planNode3,
      sinkNode: planNode2,
      meters: 0,
      segments: [],
      streets: [],
    };
    const planRoute21: PlanRoute = {
      sourceNode: planNode2,
      sinkNode: planNode1,
      meters: 0,
      segments: [],
      streets: [],
    };

    const planLegData43 = new PlanLegData(
      legEnd4,
      legEnd3,
      List([planRoute43])
    );
    const planLegData32 = new PlanLegData(
      legEnd3,
      legEnd2,
      List([planRoute32])
    );
    const planLegData21 = new PlanLegData(
      legEnd2,
      legEnd1,
      List([planRoute21])
    );

    setup.legRepository.add(planLegData43);
    setup.legRepository.add(planLegData32);
    setup.legRepository.add(planLegData21);

    const oldPlan = setup.createThreeLegPlan();

    new PlanReverser(setup.context).reverse(oldPlan).subscribe((newPlan) => {
      expect(newPlan.sourceNode.nodeId).toEqual('1004');
      expectStartFlagCoordinate(newPlan.sourceFlag, [4, 4]);

      expect(newPlan.legs.size).toEqual(3);

      const leg1 = newPlan.legs.get(0);
      expect(leg1.source.node.nodeId).toEqual(1004);
      expect(leg1.sink.node.nodeId).toEqual(1003);
      expectViaFlagCoordinate(leg1.sinkFlag, [3, 3]);
      expect(leg1.viaFlag).toEqual(null);

      const leg2 = newPlan.legs.get(1);
      expect(leg2.source.node.nodeId).toEqual(1003);
      expect(leg2.sink.node.nodeId).toEqual(1002);
      expectViaFlagCoordinate(leg2.sinkFlag, [2, 2]);
      expect(leg2.viaFlag).toEqual(null);

      const leg3 = newPlan.legs.get(2);
      expect(leg3.source.node.nodeId).toEqual(1002);
      expect(leg3.sink.node.nodeId).toEqual(1001);
      expectEndFlagCoordinate(leg3.sinkFlag, [1, 1]);
      expect(leg3.viaFlag).toEqual(null);
    });
  });

  const buildExpectedSingleViaRouteLeg = (setup: PlannerTestSetup): void => {
    const sourceNode = PlanUtil.planNodeWithCoordinate('1003', '03', null, [
      3,
      3,
    ]);
    const sinkNode = PlanUtil.planNodeWithCoordinate('1001', '01', null, [
      1,
      1,
    ]);

    const trackPathKey: TrackPathKey = {
      routeId: 10,
      pathId: 1,
    };
    const source = PlanUtil.legEndNode(1003);
    const sink: LegEnd = {
      node: null,
      route: {
        trackPathKeys: [trackPathKey],
        selection: null,
      },
    };
    const planRoute: PlanRoute = {
      sourceNode,
      sinkNode,
      meters: 0,
      segments: [],
      streets: [],
    };
    const planLegData = new PlanLegData(source, sink, List([planRoute]));

    setup.legRepository.add(planLegData);
  };

  const buildSingleViaRoutePlan = (setup: PlannerTestSetup): Plan => {
    const sourceNode = setup.node1;
    const viaNode = setup.node2;
    const sinkNode = setup.node3;

    const sourceFlag = PlanFlag.start('sourceFlag', sourceNode.coordinate);
    const viaFlag = PlanFlag.via('viaFlag', viaNode.coordinate);
    const sinkFlag = PlanFlag.end('sinkFlag', sinkNode.coordinate);
    const trackPathKey: TrackPathKey = {
      routeId: 10,
      pathId: 1,
    };

    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink: LegEnd = {
      node: null,
      route: {
        trackPathKeys: [trackPathKey],
        selection: null,
      },
    };
    const legKey = PlanUtil.key(source, sink);

    const fragment: PlanFragment = {
      meters: 0,
      orientation: 0,
      streetIndex: -1,
      coordinate: sinkNode.coordinate,
      latLon: sinkNode.latLon,
    };
    const segment: PlanSegment = {
      meters: 0,
      surface: '',
      colour: null,
      fragments: [fragment],
    };
    const route: PlanRoute = {
      sourceNode,
      sinkNode,
      meters: 0,
      segments: [segment],
      streets: [],
    };
    const leg = new PlanLeg(
      '11',
      legKey,
      source,
      sink,
      sinkFlag,
      viaFlag,
      List([route])
    );

    const oldPlan = new Plan(sourceNode, sourceFlag, List([leg]));
    setup.context.updatePlan(oldPlan);
    setup.markerLayer.addFlag(sourceFlag);
    setup.markerLayer.addFlag(viaFlag);
    setup.markerLayer.addFlag(sinkFlag);
    setup.routeLayer.addPlanLeg(leg);

    return oldPlan;
  };

  it('reverse plan with single via-route leg', () => {
    const setup = new PlannerTestSetup();

    buildExpectedSingleViaRouteLeg(setup);
    const oldPlan = buildSingleViaRoutePlan(setup);

    new PlanReverser(setup.context).reverse(oldPlan).subscribe((newPlan) => {
      expect(newPlan.sourceNode.nodeId).toEqual('1003');
      expectStartFlagCoordinate(newPlan.sourceFlag, [3, 3]);

      expect(newPlan.legs.size).toEqual(1);
      const leg = newPlan.legs.get(0);
      expect(leg.source.node.nodeId).toEqual(1003);
      expect(leg.sink.route.trackPathKeys.length).toEqual(1);
      expect(leg.sink.route.trackPathKeys[0].routeId).toEqual(10);
      expect(leg.sink.route.trackPathKeys[0].pathId).toEqual(1);
      expectViaFlagCoordinate(leg.viaFlag, [2, 2]);
      expectEndFlagCoordinate(leg.sinkFlag, [1, 1]);
    });
  });

  const buildExpectedViaRouteLeg = (setup: PlannerTestSetup): void => {
    const sourceNode = PlanUtil.planNodeWithCoordinate('1004', '04', null, [
      4,
      4,
    ]);
    const sinkNode = PlanUtil.planNodeWithCoordinate('1002', '02', null, [
      2,
      2,
    ]);
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndRoute([{ routeId: 10, pathId: 1 }]);

    const planRoute = PlanUtil.planRoute(sourceNode, sinkNode);
    const planLegData = new PlanLegData(source, sink, List([planRoute]));

    setup.legRepository.add(planLegData);
  };

  const buildExpectedNodeToNodeLeg = (setup: PlannerTestSetup): void => {
    const sourceNode = PlanUtil.planNodeWithCoordinate('1002', '02', null, [
      2,
      2,
    ]);
    const sinkNode = PlanUtil.planNodeWithCoordinate('1001', '01', null, [
      1,
      1,
    ]);
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);

    const planRoute = PlanUtil.planRoute(sourceNode, sinkNode);
    const planLegData = new PlanLegData(source, sink, List([planRoute]));

    setup.legRepository.add(planLegData);
  };

  const buildPlan = (setup: PlannerTestSetup): Plan => {
    /*
      1  -----  2  --via3--  4
    */

    const sourceNode = setup.node1;
    const viaNode = setup.node3;
    const sinkNode = setup.node4;

    const sourceFlag = PlanFlag.start('sourceFlag', sourceNode.coordinate);
    const viaFlag = PlanFlag.via('viaFlag', viaNode.coordinate);
    const sinkFlag = PlanFlag.end('sinkFlag', sinkNode.coordinate);
    const trackPathKey: TrackPathKey = { routeId: 10, pathId: 1 };

    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink: LegEnd = {
      node: null,
      route: {
        trackPathKeys: [trackPathKey],
        selection: null,
      },
    };
    const legKey = PlanUtil.key(source, sink);
    const route1 = PlanUtil.planRoute(sourceNode, setup.node2);
    const route2 = PlanUtil.planRoute(setup.node2, sinkNode);

    const leg = new PlanLeg(
      '11',
      legKey,
      source,
      sink,
      sinkFlag,
      viaFlag,
      List([route1, route2])
    );

    const oldPlan = new Plan(sourceNode, sourceFlag, List([leg]));
    setup.context.updatePlan(oldPlan);
    setup.markerLayer.addFlag(sourceFlag);
    setup.markerLayer.addFlag(viaFlag);
    setup.markerLayer.addFlag(sinkFlag);
    setup.routeLayer.addPlanLeg(leg);

    return oldPlan;
  };

  it('reverse plan with node-to-node leg and via-route leg', () => {
    const setup = new PlannerTestSetup();
    buildExpectedViaRouteLeg(setup);
    buildExpectedNodeToNodeLeg(setup);
    const oldPlan = buildPlan(setup);

    new PlanReverser(setup.context).reverse(oldPlan).subscribe((newPlan) => {
      expect(newPlan.sourceNode.nodeId).toEqual('1004');
      expectStartFlagCoordinate(newPlan.sourceFlag, [4, 4]);

      expect(newPlan.legs.size).toEqual(2);

      const leg1 = newPlan.legs.get(0);
      expect(leg1.source.node.nodeId).toEqual(1004);
      expect(leg1.sink.route.trackPathKeys.length).toEqual(1);
      expect(leg1.sink.route.trackPathKeys[0].routeId).toEqual(10);
      expect(leg1.sink.route.trackPathKeys[0].pathId).toEqual(1);
      expectViaFlagCoordinate(leg1.viaFlag, [3, 3]);
      expectInvisibleFlagCoordinate(leg1.sinkFlag, [2, 2]);

      const leg2 = newPlan.legs.get(1);
      expect(leg2.source.node.nodeId).toEqual(1002);
      expect(leg2.sink.node.nodeId).toEqual(1001);
      expect(leg2.viaFlag).toEqual(null);
      expectEndFlagCoordinate(leg2.sinkFlag, [1, 1]);
    });
  });
});
