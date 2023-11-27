import { List } from 'immutable';
import { FlagFeature } from '../features/flag-feature';
import { Plan } from '../plan/plan';
import { PlanFlag } from '../plan/plan-flag';
import { PlanUtil } from '../plan/plan-util';
import { PlannerDragFlagAnalyzer } from './planner-drag-flag-analyzer';

describe('PlannerDragFlagAnalyzer', () => {
  const node1 = PlanUtil.planNodeWithCoordinate('1001', '01', null, [1, 0]);
  const node2 = PlanUtil.planNodeWithCoordinate('1002', '02', null, [2, 0]);
  const node3 = PlanUtil.planNodeWithCoordinate('1003', '03', null, [3, 0]);
  const node4 = PlanUtil.planNodeWithCoordinate('1004', '03', null, [4, 0]);

  const sourceFlag = PlanFlag.start('sourceFlag', node1.coordinate);
  const sinkFlag1 = PlanFlag.via('sinkFlag1', node2.coordinate);
  const sinkFlag2 = PlanFlag.via('sinkFlag2', node3.coordinate);
  const sinkFlag3 = PlanFlag.end('sinkFlag3', node4.coordinate);

  const leg12 = PlanUtil.singleRoutePlanLeg('12', node1, node2, sinkFlag1, null);
  const leg23 = PlanUtil.singleRoutePlanLeg('23', node2, node3, sinkFlag2, null);
  const leg34 = PlanUtil.singleRoutePlanLeg('34', node3, node4, sinkFlag3, null);

  it('start start-point drag', () => {
    const plan = new Plan(node1, sourceFlag, List([leg12, leg23, leg34]));
    const flag = FlagFeature.start(sourceFlag.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);

    expect(drag.anchor1).toEqual(node1.coordinate);
    expect(drag.anchor2).toEqual(node1.coordinate);
    expect(drag.oldNode.nodeId).toEqual('1001');
  });

  it('start sinkFlag1 drag', () => {
    const plan = new Plan(node1, sourceFlag, List([leg12, leg23, leg34]));
    const flag = FlagFeature.via(sinkFlag1.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);

    expect(drag.anchor1).toEqual(node1.coordinate);
    expect(drag.anchor2).toEqual(node3.coordinate);
    expect(drag.oldNode.nodeId).toEqual('1002');
  });

  it('start sinkFlag2 drag', () => {
    const plan = new Plan(node1, sourceFlag, List([leg12, leg23, leg34]));
    const flag = FlagFeature.via(sinkFlag2.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);

    expect(drag.anchor1).toEqual(node2.coordinate);
    expect(drag.anchor2).toEqual(node4.coordinate);
    expect(drag.oldNode.nodeId).toEqual('1003');
  });

  it('cannot initiate drag of via node on plan with no legs', () => {
    const plan = new Plan(node1, sourceFlag, List());
    const flag = FlagFeature.via('bla');
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);
    expect(drag).toBeNull();
  });

  it('end-point', () => {
    const plan = new Plan(node1, sourceFlag, List([leg12, leg23, leg34]));
    const flag = FlagFeature.end(sinkFlag3.featureId);
    const drag = new PlannerDragFlagAnalyzer(plan).dragStarted(flag);

    expect(drag.anchor1).toEqual(node4.coordinate);
    expect(drag.anchor2).toEqual(node4.coordinate);
    expect(drag.oldNode.nodeId).toEqual('1004');
  });
});
