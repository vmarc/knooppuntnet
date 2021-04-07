import { List } from 'immutable';
import { expectStartFlag } from '../../../util/test-support';
import { PlannerTestSetup } from '../context/planner-test-setup';
import { Plan } from '../plan/plan';
import { PlanFlag } from '../plan/plan-flag';
import { PlanUtil } from '../plan/plan-util';
import { PlannerCommandMoveFirstLegSource } from './planner-command-move-first-leg-source';

describe('PlannerCommandMoveFirstLegSource', () => {
  it('move start point - do and undo', () => {
    const setup = new PlannerTestSetup();

    const oldSinkFlag = PlanFlag.end('oldSinkFlag', [2, 2]);
    const newSinkFlag = PlanFlag.end('newSinkFlag', [2, 2]);

    const oldLeg = PlanUtil.singleRoutePlanLeg(
      '12',
      setup.node1,
      setup.node2,
      oldSinkFlag,
      null
    );
    const newLeg = PlanUtil.singleRoutePlanLeg(
      '32',
      setup.node3,
      setup.node2,
      newSinkFlag,
      null
    );

    const oldSourceFlag = PlanFlag.start('oldStartFlag', [1, 1]);
    const newSourceFlag = PlanFlag.start('newStartFlag', [3, 3]);

    const plan = new Plan(setup.node1, oldSourceFlag, List([oldLeg]));
    setup.context.updatePlan(plan);

    const command = new PlannerCommandMoveFirstLegSource(
      oldLeg,
      setup.node1,
      oldSourceFlag,
      newLeg,
      setup.node3,
      newSourceFlag
    );

    setup.context.execute(command);

    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual('32');
    expect(setup.context.plan.sourceNode.nodeId).toEqual('1003');
    expectStartFlag(setup.context.plan.sourceFlag, 'newStartFlag', [3, 3]);

    setup.markerLayer.expectFlagCount(2);
    setup.markerLayer.expectStartFlagExists('newStartFlag', [3, 3]);
    setup.markerLayer.expectEndFlagExists('newSinkFlag', [2, 2]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists('32', newLeg);

    command.undo(setup.context);

    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual('12');
    expect(setup.context.plan.sourceNode.nodeId).toEqual('1001');
    expectStartFlag(setup.context.plan.sourceFlag, 'oldStartFlag', [1, 1]);

    setup.markerLayer.expectFlagCount(2);
    setup.markerLayer.expectStartFlagExists('oldStartFlag', [1, 1]);
    setup.markerLayer.expectEndFlagExists('oldSinkFlag', [2, 2]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists('12', oldLeg);

    command.do(setup.context);

    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual('32');
    expect(setup.context.plan.sourceNode.nodeId).toEqual('1003');
    expectStartFlag(setup.context.plan.sourceFlag, 'newStartFlag', [3, 3]);

    setup.markerLayer.expectFlagCount(2);
    setup.markerLayer.expectStartFlagExists('newStartFlag', [3, 3]);
    setup.markerLayer.expectEndFlagExists('newSinkFlag', [2, 2]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists('32', newLeg);
  });

  it('move start point (first leg is via-route) - do and undo', () => {
    const setup = new PlannerTestSetup();

    const oldViaFlag = PlanFlag.via('oldViaFlag', [2.5, 2.5]);
    const oldSinkFlag = PlanFlag.end('oldSinkFlag', [2, 2]);
    const newSinkFlag = PlanFlag.end('newSinkFlag', [2, 2]);

    const oldLeg = PlanUtil.singleRoutePlanLeg(
      '12',
      setup.node1,
      setup.node2,
      oldSinkFlag,
      oldViaFlag
    );
    const newLeg = PlanUtil.singleRoutePlanLeg(
      '32',
      setup.node3,
      setup.node2,
      newSinkFlag,
      null
    );

    const oldSourceFlag = PlanFlag.start('oldStartFlag', [1, 1]);
    const newSourceFlag = PlanFlag.start('newStartFlag', [3, 3]);

    const plan = new Plan(setup.node1, oldSourceFlag, List([oldLeg]));
    setup.context.updatePlan(plan);

    const command = new PlannerCommandMoveFirstLegSource(
      oldLeg,
      setup.node1,
      oldSourceFlag,
      newLeg,
      setup.node3,
      newSourceFlag
    );

    setup.context.execute(command);

    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual('32');
    expect(setup.context.plan.sourceNode.nodeId).toEqual('1003');
    expectStartFlag(setup.context.plan.sourceFlag, 'newStartFlag', [3, 3]);

    setup.markerLayer.expectFlagCount(2);
    setup.markerLayer.expectStartFlagExists('newStartFlag', [3, 3]);
    setup.markerLayer.expectEndFlagExists('newSinkFlag', [2, 2]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists('32', newLeg);

    command.undo(setup.context);

    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual('12');
    expect(setup.context.plan.sourceNode.nodeId).toEqual('1001');
    expectStartFlag(setup.context.plan.sourceFlag, 'oldStartFlag', [1, 1]);

    setup.markerLayer.expectFlagCount(3);
    setup.markerLayer.expectStartFlagExists('oldStartFlag', [1, 1]);
    setup.markerLayer.expectViaFlagExists('oldViaFlag', [2.5, 2.5]);
    setup.markerLayer.expectEndFlagExists('oldSinkFlag', [2, 2]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists('12', oldLeg);

    command.do(setup.context);

    expect(setup.context.plan.legs.size).toEqual(1);
    expect(setup.context.plan.legs.get(0).featureId).toEqual('32');
    expect(setup.context.plan.sourceNode.nodeId).toEqual('1003');
    expectStartFlag(setup.context.plan.sourceFlag, 'newStartFlag', [3, 3]);

    setup.markerLayer.expectFlagCount(2);
    setup.markerLayer.expectStartFlagExists('newStartFlag', [3, 3]);
    setup.markerLayer.expectEndFlagExists('newSinkFlag', [2, 2]);
    setup.routeLayer.expectRouteLegCount(1);
    setup.routeLayer.expectRouteLegExists('32', newLeg);
  });
});
