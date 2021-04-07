import { PlannerTestSetup } from '../context/planner-test-setup';
import { PlanFlag } from '../plan/plan-flag';
import { PlanReverser } from '../plan/plan-reverser';
import { PlanUtil } from '../plan/plan-util';
import { PlannerCommandAddLeg } from './planner-command-add-leg';
import { PlannerCommandAddStartPoint } from './planner-command-add-start-point';
import { PlannerCommandReverse } from './planner-command-reverse';
import { expectViaFlagCoordinate } from '../../../util/test-support';
import { expectEndFlagCoordinate } from '../../../util/test-support';

describe('PlannerCommandReverse', () => {
  it('do and undo', () => {
    const setup = new PlannerTestSetup();

    const sourceFlag = PlanFlag.start('sourceFlag', [1, 1]);
    const sinkFlag1 = PlanFlag.via('sinkFlag1', [2, 2]);
    const sinkFlag2 = PlanFlag.end('sinkFlag2', [3, 3]);

    const leg1 = PlanUtil.singleRoutePlanLeg(
      '12',
      setup.node1,
      setup.node2,
      sinkFlag1,
      null
    );
    const leg2 = PlanUtil.singleRoutePlanLeg(
      '23',
      setup.node2,
      setup.node3,
      sinkFlag2,
      null
    );

    setup.createPlanLegData(setup.node3, setup.node2);
    setup.createPlanLegData(setup.node2, setup.node1);

    setup.context.execute(
      new PlannerCommandAddStartPoint(setup.node1, sourceFlag)
    );
    setup.context.execute(new PlannerCommandAddLeg(leg1));
    setup.context.execute(new PlannerCommandAddLeg(leg2));

    setup.markerLayer.expectFlagCount(3);
    setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
    setup.markerLayer.expectViaFlagExists('sinkFlag1', [2, 2]);
    setup.markerLayer.expectEndFlagExists('sinkFlag2', [3, 3]);
    setup.routeLayer.expectRouteLegExists('12', leg1);
    setup.routeLayer.expectRouteLegExists('23', leg2);

    expect(setup.context.plan.legs.size).toEqual(2);
    expect(setup.context.plan.legs.get(0).featureId).toEqual('12');
    expect(setup.context.plan.legs.get(1).featureId).toEqual('23');

    new PlanReverser(setup.context)
      .reverse(setup.context.plan)
      .subscribe((newPlan) => {
        const reverseCommand = new PlannerCommandReverse(
          setup.context.plan,
          newPlan
        );
        setup.context.execute(reverseCommand);

        setup.markerLayer.expectFlagCount(3);
        setup.markerLayer.expectPlanFlagExists(setup.context.plan.sourceFlag);
        setup.markerLayer.expectPlanFlagExists(
          setup.context.plan.legs.get(0).sinkFlag
        );
        setup.markerLayer.expectPlanFlagExists(
          setup.context.plan.legs.get(1).sinkFlag
        );

        expect(setup.context.plan.sourceNode.nodeId).toEqual('1003');
        expect(setup.context.plan.sourceFlag.coordinate).toEqual([3, 3]);

        {
          const legs = setup.context.plan.legs;
          expect(legs.size).toEqual(2);
          expectViaFlagCoordinate(legs.get(0).sinkFlag, [2, 2]);
          expectEndFlagCoordinate(legs.get(1).sinkFlag, [1, 1]);
        }

        reverseCommand.undo(setup.context);

        setup.markerLayer.expectFlagCount(3);
        setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
        setup.markerLayer.expectViaFlagExists('sinkFlag1', [2, 2]);
        setup.markerLayer.expectEndFlagExists('sinkFlag2', [3, 3]);
        setup.routeLayer.expectRouteLegExists('12', leg1);
        setup.routeLayer.expectRouteLegExists('23', leg2);

        expect(setup.context.plan.legs.size).toEqual(2);
        expect(setup.context.plan.legs.get(0).featureId).toEqual('12');
        expect(setup.context.plan.legs.get(1).featureId).toEqual('23');
      });
  });
});
