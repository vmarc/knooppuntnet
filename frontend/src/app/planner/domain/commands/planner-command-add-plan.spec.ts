import { List } from 'immutable';
import { expectEndFlag } from '../../util/test-support';
import { PlannerTestSetup } from '../context/planner-test-setup';
import { Plan } from '../plan/plan';
import { PlanFlag } from '../plan/plan-flag';
import { PlanUtil } from '../plan/plan-util';
import { PlannerCommandAddPlan } from './planner-command-add-plan';

describe('PlannerCommandAddPlan', () => {
  it('do and undo and redo', () => {
    const setup = new PlannerTestSetup();

    const startFlag = PlanFlag.start('startFlag', setup.node1.coordinate);
    const sinkFlag = PlanFlag.end('sinkFlag', setup.node2.coordinate);

    const leg = PlanUtil.singleRoutePlanLeg('12', setup.node1, setup.node2, sinkFlag, null);

    const plan = new Plan(setup.node1, startFlag, List([leg]));

    const command = new PlannerCommandAddPlan(plan);
    setup.context.execute(command);

    setup.markerLayer.expectFlagCount(2);
    setup.markerLayer.expectStartFlagExists('startFlag', [1, 1]);
    setup.markerLayer.expectEndFlagExists('sinkFlag', [2, 2]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists('12', leg);

    {
      const legs = setup.context.plan.legs;
      expect(legs.size).toEqual(1);

      const leg1 = legs.get(0);
      expect(leg1.featureId).toEqual('12');
      expectEndFlag(leg1.sinkFlag, 'sinkFlag', [2, 2]);
      expect(leg1.viaFlag).toEqual(null);
    }

    command.undo(setup.context);

    setup.markerLayer.expectFlagCount(0);
    setup.routeLayer.expectRouteLegCount(0);
    expect(setup.context.plan.legs.size).toEqual(0);

    command.do(setup.context);

    setup.markerLayer.expectFlagCount(2);
    setup.markerLayer.expectStartFlagExists('startFlag', [1, 1]);
    setup.markerLayer.expectEndFlagExists('sinkFlag', [2, 2]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists('12', leg);

    {
      const legs = setup.context.plan.legs;
      expect(legs.size).toEqual(1);

      const leg1 = legs.get(0);
      expect(leg1.featureId).toEqual('12');
      expectEndFlag(leg1.sinkFlag, 'sinkFlag', [2, 2]);
      expect(leg1.viaFlag).toEqual(null);
    }
  });
});
