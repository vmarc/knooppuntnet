import { List } from 'immutable';
import { expectEndFlag } from '../../util/test-support';
import { PlannerTestSetup } from '../context/planner-test-setup';
import { Plan } from '../plan/plan';
import { PlanFlag } from '../plan/plan-flag';
import { PlanUtil } from '../plan/plan-util';
import { PlannerCommandAddPlan } from './planner-command-add-plan';
import { PlannerCommandReplaceLeg } from './planner-command-replace-leg';

describe('PlannerCommandReplaceLeg', () => {
  it('move end point - do, undo and redo', () => {
    const setup = new PlannerTestSetup();

    const sourceFlag = PlanFlag.start('sourceFlag', [1, 1]);
    const oldSinkFlag = PlanFlag.end('oldSinkFlag', [2, 2]);
    const newSinkFlag = PlanFlag.end('newSinkFlag', [3, 3]);

    const oldLeg = PlanUtil.singleRoutePlanLeg('12', setup.node1, setup.node2, oldSinkFlag, null);
    const newLeg = PlanUtil.singleRoutePlanLeg('13', setup.node1, setup.node3, newSinkFlag, null);

    const plan = new Plan(setup.node1, sourceFlag, List([oldLeg]));
    setup.context.execute(new PlannerCommandAddPlan(plan));

    const command = new PlannerCommandReplaceLeg(oldLeg, newLeg);
    setup.context.execute(command);

    setup.markerLayer.expectFlagCount(2);
    setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
    setup.markerLayer.expectEndFlagExists('newSinkFlag', [3, 3]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists('13', newLeg);

    expect(setup.context.plan.sourceNode.nodeId).toEqual('1001');

    {
      const legs = setup.context.plan.legs;
      expect(legs.size).toEqual(1);

      const leg = legs.get(0);
      expect(leg.featureId).toEqual('13');
      expect(leg.sourceNode.nodeId).toEqual('1001');
      expect(leg.sinkNode.nodeId).toEqual('1003');
      expectEndFlag(leg.sinkFlag, 'newSinkFlag', [3, 3]);
    }

    command.undo(setup.context);

    setup.markerLayer.expectFlagCount(2);
    setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
    setup.markerLayer.expectEndFlagExists('oldSinkFlag', [2, 2]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists('12', oldLeg);

    expect(setup.context.plan.sourceNode.nodeId).toEqual('1001');
    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual('12');
    expectEndFlag(setup.context.plan.legs.get(0).sinkFlag, 'oldSinkFlag', [2, 2]);

    command.do(setup.context);

    setup.markerLayer.expectFlagCount(2);
    setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
    setup.markerLayer.expectEndFlagExists('newSinkFlag', [3, 3]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists('13', newLeg);

    expect(setup.context.plan.sourceNode.nodeId).toEqual('1001');

    {
      const legs = setup.context.plan.legs;
      expect(legs.size).toEqual(1);

      const leg = legs.get(0);
      expect(leg.featureId).toEqual('13');
      expect(leg.sourceNode.nodeId).toEqual('1001');
      expect(leg.sinkNode.nodeId).toEqual('1003');
      expectEndFlag(leg.sinkFlag, 'newSinkFlag', [3, 3]);
    }
  });

  it('move end point to via-route - do, undo and redo', () => {
    const setup = new PlannerTestSetup();

    const sourceFlag = PlanFlag.start('sourceFlag', [1, 1]);
    const oldSinkFlag = PlanFlag.end('oldSinkFlag', [2, 2]);
    const newSinkFlag = PlanFlag.end('newSinkFlag', [3, 3]);
    const newViaFlag = PlanFlag.via('newViaFlag', [3.5, 3.5]);

    const oldLeg = PlanUtil.singleRoutePlanLeg('12', setup.node1, setup.node2, oldSinkFlag, null);
    const newLeg = PlanUtil.singleRoutePlanLeg(
      '13',
      setup.node1,
      setup.node3,
      newSinkFlag,
      newViaFlag
    );

    const plan = new Plan(setup.node1, sourceFlag, List([oldLeg]));
    setup.context.execute(new PlannerCommandAddPlan(plan));

    const command = new PlannerCommandReplaceLeg(oldLeg, newLeg);
    setup.context.execute(command);

    setup.markerLayer.expectFlagCount(3);
    setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
    setup.markerLayer.expectViaFlagExists('newViaFlag', [3.5, 3.5]);
    setup.markerLayer.expectEndFlagExists('newSinkFlag', [3, 3]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists('13', newLeg);

    expect(setup.context.plan.sourceNode.nodeId).toEqual('1001');
    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual('13');
    expectEndFlag(setup.context.plan.legs.get(0).sinkFlag, 'newSinkFlag', [3, 3]);

    command.undo(setup.context);

    setup.markerLayer.expectFlagCount(2);
    setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
    setup.markerLayer.expectEndFlagExists('oldSinkFlag', [2, 2]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists('12', oldLeg);

    expect(setup.context.plan.sourceNode.nodeId).toEqual('1001');
    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual('12');
    expectEndFlag(setup.context.plan.legs.get(0).sinkFlag, 'oldSinkFlag', [2, 2]);

    command.do(setup.context);

    setup.markerLayer.expectFlagCount(3);
    setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
    setup.markerLayer.expectViaFlagExists('newViaFlag', [3.5, 3.5]);
    setup.markerLayer.expectEndFlagExists('newSinkFlag', [3, 3]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists('13', newLeg);

    expect(setup.context.plan.sourceNode.nodeId).toEqual('1001');
    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual('13');
    expectEndFlag(setup.context.plan.legs.get(0).sinkFlag, 'newSinkFlag', [3, 3]);
  });
});
