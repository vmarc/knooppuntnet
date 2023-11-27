import { PlannerTestSetup } from '../context/planner-test-setup';
import { PlanFlag } from '../plan/plan-flag';
import { PlanUtil } from '../plan/plan-util';
import { PlannerCommandAddLeg } from './planner-command-add-leg';
import { PlannerCommandAddStartPoint } from './planner-command-add-start-point';
import { PlannerCommandMoveViaPointToViaRoute } from './planner-command-move-via-point-to-via-route';

describe('PlannerCommandMoveViaPointToViaRoute', () => {
  it('do and undo', () => {
    const setup = new PlannerTestSetup();

    const sourceFlag = PlanFlag.start('sourceFlag', [1, 1]);
    const oldViaFlag = PlanFlag.via('oldViaFlag', [2, 2]);
    const sinkFlag = PlanFlag.end('sinkFlag', [3, 3]);

    const newViaFlag = PlanFlag.via('newViaFlag', [4.5, 4.5]);
    const newLeg1SinkFlag = PlanFlag.via('newLeg1SinkFlag', [4, 4]);

    const oldLeg1 = PlanUtil.singleRoutePlanLeg('12', setup.node1, setup.node2, oldViaFlag, null);
    const oldLeg2 = PlanUtil.singleRoutePlanLeg('23', setup.node2, setup.node3, sinkFlag, null);
    const newLeg1 = PlanUtil.singleRoutePlanLeg(
      '14',
      setup.node1,
      setup.node4,
      newLeg1SinkFlag,
      newViaFlag
    );
    const newLeg2 = PlanUtil.singleRoutePlanLeg('43', setup.node4, setup.node3, sinkFlag, null);

    setup.context.execute(new PlannerCommandAddStartPoint(setup.node1, sourceFlag));
    setup.context.execute(new PlannerCommandAddLeg(oldLeg1));
    setup.context.execute(new PlannerCommandAddLeg(oldLeg2));

    setup.markerLayer.expectFlagCount(3);
    setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
    setup.markerLayer.expectViaFlagExists('oldViaFlag', [2, 2]);
    setup.markerLayer.expectEndFlagExists('sinkFlag', [3, 3]);
    setup.routeLayer.expectRouteLegExists('12', oldLeg1);
    setup.routeLayer.expectRouteLegExists('23', oldLeg2);

    {
      const legs = setup.context.plan.legs;
      expect(legs.size).toEqual(2);

      const leg1 = legs.get(0);
      expect(leg1.featureId).toEqual('12');
      expect(leg1.sourceNode.nodeId).toEqual('1001');
      expect(leg1.sinkNode.nodeId).toEqual('1002');

      const leg2 = legs.get(1);
      expect(leg2.featureId).toEqual('23');
      expect(leg2.sourceNode.nodeId).toEqual('1002');
      expect(leg2.sinkNode.nodeId).toEqual('1003');
    }

    const command = new PlannerCommandMoveViaPointToViaRoute(oldLeg1, oldLeg2, newLeg1, newLeg2);
    setup.context.execute(command);

    setup.markerLayer.expectFlagCount(4);
    setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
    setup.markerLayer.expectViaFlagExists('newViaFlag', [4.5, 4.5]);
    setup.markerLayer.expectViaFlagExists('newLeg1SinkFlag', [4, 4]);
    setup.markerLayer.expectEndFlagExists('sinkFlag', [3, 3]);

    setup.routeLayer.expectRouteLegExists('14', newLeg1);
    setup.routeLayer.expectRouteLegExists('43', newLeg2);

    {
      const legs = setup.context.plan.legs;
      expect(legs.size).toEqual(2);

      const leg1 = legs.get(0);
      expect(leg1.featureId).toEqual('14');
      expect(leg1.sourceNode.nodeId).toEqual('1001');
      expect(leg1.sinkNode.nodeId).toEqual('1004');

      const leg2 = legs.get(1);
      expect(leg2.featureId).toEqual('43');
      expect(leg2.sourceNode.nodeId).toEqual('1004');
      expect(leg2.sinkNode.nodeId).toEqual('1003');
    }

    command.undo(setup.context);

    setup.markerLayer.expectFlagCount(3);
    setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
    setup.markerLayer.expectViaFlagExists('oldViaFlag', [2, 2]);
    setup.markerLayer.expectEndFlagExists('sinkFlag', [3, 3]);
    setup.routeLayer.expectRouteLegExists('12', oldLeg1);
    setup.routeLayer.expectRouteLegExists('23', oldLeg2);

    {
      const legs = setup.context.plan.legs;
      expect(legs.size).toEqual(2);
      expect(legs.get(0).featureId).toEqual('12');
      expect(legs.get(1).featureId).toEqual('23');
    }
  });
});
