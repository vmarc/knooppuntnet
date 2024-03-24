import { expectStartFlag } from '../../util/test-support';
import { PlannerTestSetup } from '../context/planner-test-setup';
import { PlanFlag } from '../plan/plan-flag';
import { PlannerCommandAddStartPoint } from './planner-command-add-start-point';

describe('PlannerCommandAddStartPoint', () => {
  it('add start point - do and undo', () => {
    const setup = new PlannerTestSetup();
    const plan = setup.context.plan;

    const startFlag = PlanFlag.start('startFlag', setup.node1.coordinate);

    const command = new PlannerCommandAddStartPoint(setup.node1, startFlag);
    setup.context.execute(command);

    setup.markerLayer.expectFlagCount(1);
    setup.markerLayer.expectStartFlagExists('startFlag', [1, 1]);

    expect(plan().sourceNode.nodeId).toEqual('1001');
    expectStartFlag(plan().sourceFlag, 'startFlag', [1, 1]);
    expect(plan().legs.size).toEqual(0);

    command.undo(setup.context);

    setup.markerLayer.expectFlagCount(0);
    expect(plan().sourceNode).toEqual(null);
    expect(plan().legs.size).toEqual(0);

    command.do(setup.context);

    setup.markerLayer.expectFlagCount(1);
    setup.markerLayer.expectStartFlagExists('startFlag', [1, 1]);

    expect(plan().sourceNode.nodeId).toEqual('1001');
    expectStartFlag(plan().sourceFlag, 'startFlag', [1, 1]);
    expect(plan().legs.size).toEqual(0);
  });
});
