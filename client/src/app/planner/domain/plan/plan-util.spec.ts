import { PlanRoute } from '@api/common/planner';
import { List } from 'immutable';
import { Plan } from './plan';
import { PlanFlag } from './plan-flag';
import { PlanLeg } from './plan-leg';
import { PlanUtil } from './plan-util';

describe('PlanUtil', () => {
  it('toUrlString - empty plan', () => {
    expect(PlanUtil.toUrlString(Plan.empty)).toEqual('');
  });

  it('toUrlString - plan with source only', () => {
    const startNode = PlanUtil.planNode('10', '', null, {
      latitude: '',
      longitude: '',
    });
    const startFlag = PlanFlag.start('n1', [1, 1]);
    const plan = new Plan(startNode, startFlag, List());
    expect(PlanUtil.toUrlString(plan)).toEqual('a');
  });

  it('toUrlString - plan with multiple legs', () => {
    const startNode = PlanUtil.planNode('10', '', null, {
      latitude: '',
      longitude: '',
    });
    const viaNode1 = PlanUtil.planNode('11', '', null, {
      latitude: '',
      longitude: '',
    });
    const viaNode2 = PlanUtil.planNode('12', '', null, {
      latitude: '',
      longitude: '',
    });
    const endNode = PlanUtil.planNode('13', '', null, {
      latitude: '',
      longitude: '',
    });

    const route1: PlanRoute = {
      sourceNode: startNode,
      sinkNode: viaNode1,
      meters: 0,
      segments: [],
      streets: [],
    };
    const route2: PlanRoute = {
      sourceNode: viaNode1,
      sinkNode: viaNode2,
      meters: 0,
      segments: [],
      streets: [],
    };
    const route3: PlanRoute = {
      sourceNode: viaNode2,
      sinkNode: endNode,
      meters: 0,
      segments: [],
      streets: [],
    };

    const startLegEnd = PlanUtil.legEndNode(10);
    const viaLegEnd1 = PlanUtil.legEndNode(11);
    const viaLegEnd2 = PlanUtil.legEndNode(12);
    const endLegEnd = PlanUtil.legEndNode(13);

    const leg1 = new PlanLeg(
      '',
      '',
      startLegEnd,
      viaLegEnd1,
      null,
      null,
      List([route1])
    );
    const leg2 = new PlanLeg(
      '',
      '',
      viaLegEnd1,
      viaLegEnd2,
      null,
      null,
      List([route2])
    );
    const leg3 = new PlanLeg(
      '',
      '',
      viaLegEnd2,
      endLegEnd,
      null,
      null,
      List([route3])
    );

    const startFlag = PlanFlag.start('n1', startNode.coordinate);
    const plan = new Plan(startNode, startFlag, List([leg1, leg2, leg3]));

    expect(PlanUtil.toUrlString(plan)).toEqual('a-b-c-d');
  });

  it('toNodeIds', () => {
    const nodeIds = PlanUtil.toNodeIds('a-b-c-d');
    expect(nodeIds.size).toEqual(4);
    expect(nodeIds.get(0)).toEqual('10');
    expect(nodeIds.get(1)).toEqual('11');
    expect(nodeIds.get(2)).toEqual('12');
    expect(nodeIds.get(3)).toEqual('13');
  });

  it('distinct colours', () => {
    const colours = List(['red', 'red', 'blue', 'red', 'red', 'green']);
    const distinctColours = PlanUtil.distinctColours(colours);
    expect(distinctColours.toArray()).toEqual(['red', 'blue', 'red', 'green']);
  });

  it('total distance empty plan', () => {
    expect(Plan.empty.cumulativeKmLeg(0)).toEqual('0 km');
  });

  it('total distance', () => {
    const route1: PlanRoute = {
      sourceNode: null,
      sinkNode: null,
      meters: 1000,
      segments: [],
      streets: [],
    };
    const route2: PlanRoute = {
      sourceNode: null,
      sinkNode: null,
      meters: 2000,
      segments: [],
      streets: [],
    };
    const route3: PlanRoute = {
      sourceNode: null,
      sinkNode: null,
      meters: 4000,
      segments: [],
      streets: [],
    };

    const leg1 = new PlanLeg(
      '1',
      '',
      null,
      null,
      null,
      null,
      List([route1, route2])
    );
    const leg2 = new PlanLeg('2', '', null, null, null, null, List([route3]));

    const plan = new Plan(null, null, List([leg1, leg2]));

    expect(plan.cumulativeKmLeg(0)).toEqual('3 km');
    expect(plan.cumulativeKmLeg(1)).toEqual('7 km');
  });
});
