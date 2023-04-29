import { List } from 'immutable';
import { expectCoordinates } from '../../util/test-support';
import { expectStartFlag } from '../../util/test-support';
import { expectEndFlagCoordinate } from '../../util/test-support';
import { PlannerCommandMoveFirstLegSource } from '../commands/planner-command-move-first-leg-source';
import { PlannerCommandMoveStartPoint } from '../commands/planner-command-move-start-point';
import { PlannerCommandMoveViaPoint } from '../commands/planner-command-move-via-point';
import { PlannerCommandReplaceLeg } from '../commands/planner-command-replace-leg';
import { PlannerCommandSplitLeg } from '../commands/planner-command-split-leg';
import { PlannerTestSetup } from '../context/planner-test-setup';
import { FlagFeature } from '../features/flag-feature';
import { LegFeature } from '../features/leg-feature';
import { MapFeature } from '../features/map-feature';
import { NetworkNodeFeature } from '../features/network-node-feature';
import { Plan } from '../plan/plan';
import { PlanFlag } from '../plan/plan-flag';
import { PlanUtil } from '../plan/plan-util';
import { PlannerEngineImpl } from './planner-engine-impl';

describe('PlannerEngine', () => {
  describe('add start node', () => {
    it('should add start node upon single click while hoovering over network node', () => {
      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);

      const node = NetworkNodeFeature.create('1001', '01', null, [1, 1], false);
      const features: List<MapFeature> = List([node]);

      // act
      const eventIsFurtherPropagated = engine.handleSingleClickEvent(
        features,
        [1.1, 1.1],
        false
      );

      // assert
      expect(eventIsFurtherPropagated).toBeFalsy();

      expect(setup.context.plan.sourceNode.nodeId).toEqual('1001');
      expect(setup.context.plan.legs.size).toEqual(0);

      setup.markerLayer.expectFlagCount(1);
      setup.markerLayer.expectStartFlagExists(
        setup.context.plan.sourceFlag.featureId,
        [1, 1]
      );

      expect(setup.context.commandStack.commandCount).toEqual(1);
    });
  });

  describe('add leg when plan already has start point', () => {
    it('should add new leg upon single click while hoovering over network node', () => {
      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);

      const node1 = NetworkNodeFeature.create(
        '1001',
        '01',
        null,
        [1, 1],
        false
      );
      const node2 = NetworkNodeFeature.create(
        '1002',
        '02',
        null,
        [2, 2],
        false
      );

      setup.createPlanLegData(setup.node1, setup.node2);

      setup.context.updatePlan(
        new Plan(node1.node, PlanFlag.start('n1', [1, 1]), List())
      );

      const features: List<MapFeature> = List([node2]);

      // act
      const eventIsFurtherPropagated = engine.handleSingleClickEvent(
        features,
        [2.1, 2.1],
        false
      );

      // assert
      expect(eventIsFurtherPropagated).toBeFalsy();

      expect(setup.context.plan.sourceNode.nodeId).toEqual('1001');

      {
        const legs = setup.context.plan.legs;
        expect(legs.size).toEqual(1);

        const leg = legs.get(0);
        expect(leg.source.node.nodeId).toEqual(1001);
        expect(leg.sink.node.nodeId).toEqual(1002);
        expectEndFlagCoordinate(leg.sinkFlag, [2, 2]);
        expect(leg.viaFlag).toEqual(null);
      }

      setup.markerLayer.expectFlagCount(1);
      setup.markerLayer.expectEndFlagExists(
        setup.context.plan.legs.get(0).sinkFlag.featureId,
        [2, 2]
      );

      expect(setup.context.commandStack.commandCount).toEqual(1);
    });
  });

  describe('move start-node', () => {
    it('should handle start node drag, while no legs in plan yet', () => {
      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);

      setup.createPlanWithStartPointOnly();

      const oldStartNodeFeature = FlagFeature.start('sourceFlag');
      const newStartNodeFeature = NetworkNodeFeature.create(
        '1002',
        '02',
        null,
        [2, 2],
        false
      );

      // act - start drag
      const eventIsFurtherPropagated = engine.handleDragEvent(
        List([oldStartNodeFeature]),
        [1.1, 1.1]
      );

      // assert - drag started
      expect(eventIsFurtherPropagated).toBeFalsy();

      setup.markerLayer.expectFlagCount(1);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1.1, 1.1]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.1, 1.1]);

      // act - drag ongoing
      const eventIsFurtherPropagated2 = engine.handleDragEvent(
        List(),
        [1.5, 1.5]
      );

      // assert - drag ongoing
      expect(eventIsFurtherPropagated2).toBeFalsy();

      setup.markerLayer.expectFlagCount(1);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1.5, 1.5]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.5, 1.5]);

      // act - drag end
      const eventIsFurtherPropagated3 = engine.handleUpEvent(
        List([newStartNodeFeature]),
        [2.1, 2.1]
      );

      // assert - drag end
      expect(eventIsFurtherPropagated3).toBeFalsy();

      expect(setup.context.plan.sourceNode.nodeId).toEqual('1002');
      expect(setup.context.plan.legs.size).toEqual(0);

      setup.markerLayer.expectFlagCount(1);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [2, 2]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(1);
      const command = setup.context.commandStack.last();
      expect(command).toEqual(expect.any(PlannerCommandMoveStartPoint));
    });

    it('should cancel "move start-node" upon mouse up, while no legs in plan yet, and not hoovering over network node', () => {
      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);

      setup.createPlanWithStartPointOnly();

      const sourceFlag = FlagFeature.start('sourceFlag');

      // act - start drag
      const eventIsFurtherPropagated = engine.handleDragEvent(
        List([sourceFlag]),
        [1.1, 1.1]
      );

      // assert - drag started
      expect(eventIsFurtherPropagated).toBeFalsy();

      setup.markerLayer.expectFlagCount(1);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1.1, 1.1]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.1, 1.1]);

      // act - drag end
      const eventIsFurtherPropagated3 = engine.handleUpEvent(
        List(),
        [1.5, 1.5]
      );

      // assert - drag cancelled
      expect(eventIsFurtherPropagated3).toBeFalsy();

      expect(setup.context.plan.sourceNode.nodeId).toEqual('1001');
      expect(setup.context.plan.legs.size).toEqual(0);

      setup.markerLayer.expectFlagCount(1);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(0);
    });

    it('should handle start node drag, while legs in plan', () => {
      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      setup.createOneLegPlan();
      const newStartNodeFeature = NetworkNodeFeature.create(
        '1003',
        '03',
        null,
        [3, 3],
        false
      );
      const oldStartNodeFeature = FlagFeature.start('sourceFlag');

      setup.createPlanLegData(setup.node3, setup.node2);

      // act - start drag
      const eventIsFurtherPropagated = engine.handleDragEvent(
        List([oldStartNodeFeature]),
        [1.1, 1.1]
      );

      // assert - drag started
      expect(eventIsFurtherPropagated).toBeFalsy();

      setup.markerLayer.expectFlagCount(2);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1.1, 1.1]);
      setup.markerLayer.expectEndFlagExists('sinkFlag', [2, 2]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.1, 1.1]);

      // act - drag ongoing
      const eventIsFurtherPropagated2 = engine.handleDragEvent(
        List(),
        [1.5, 1.5]
      );

      // assert - drag ongoing
      expect(eventIsFurtherPropagated2).toBeFalsy();

      setup.markerLayer.expectFlagCount(2);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1.5, 1.5]);
      setup.markerLayer.expectEndFlagExists('sinkFlag', [2, 2]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.5, 1.5]);

      // act - drag end
      const eventIsFurtherPropagated3 = engine.handleUpEvent(
        List([newStartNodeFeature]),
        [3.1, 3.1]
      );

      // assert - drag end
      expect(eventIsFurtherPropagated3).toBeFalsy();

      const newPlan = setup.context.plan;
      expect(newPlan.sourceNode.nodeId).toEqual('1003');
      expect(newPlan.legs.size).toEqual(1);

      setup.markerLayer.expectFlagCount(2);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [3, 3]);
      setup.markerLayer.expectEndFlagCoordinateExists([2, 2]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(1);
      expect(setup.context.commandStack.last()).toEqual(
        expect.any(PlannerCommandMoveFirstLegSource)
      );
    });

    it('should cancel "move start-node" upon mouse up, with legs in plan, and not hoovering over network node', () => {
      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      setup.createOneLegPlan();

      const oldStartNodeFeature = FlagFeature.start('sourceFlag');

      // act - start drag
      const eventIsFurtherPropagated = engine.handleDragEvent(
        List([oldStartNodeFeature]),
        [1.1, 1.1]
      );

      // assert - drag started
      expect(eventIsFurtherPropagated).toBeFalsy();

      setup.markerLayer.expectFlagCount(2);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1.1, 1.1]);
      setup.markerLayer.expectEndFlagExists('sinkFlag', [2, 2]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.1, 1.1]);

      // act - drag end
      const eventIsFurtherPropagated3 = engine.handleUpEvent(
        List(),
        [1.5, 1.5]
      );

      // assert - drag end
      expect(eventIsFurtherPropagated3).toBeFalsy();

      const newPlan = setup.context.plan;
      expect(newPlan.sourceNode.nodeId).toEqual('1001');
      expect(newPlan.legs.size).toEqual(1);
      const newLeg = newPlan.legs.get(0);
      expect(newLeg.sourceNode.nodeId).toEqual('1001');
      expect(newLeg.sinkNode.nodeId).toEqual('1002');

      setup.markerLayer.expectFlagCount(2);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
      setup.markerLayer.expectEndFlagExists('sinkFlag', [2, 2]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(0);
    });
  });

  describe('move end-node', () => {
    it('move end-node', () => {
      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      const oldPlan = setup.createOneLegPlan();
      const newEndNodeFeature = NetworkNodeFeature.create(
        '1003',
        '03',
        null,
        [3, 3],
        false
      );
      const oldEndNodeFeature = FlagFeature.end(
        oldPlan.legs.last(null).sinkFlag.featureId
      );

      setup.createPlanLegData(setup.node1, setup.node3);

      // act - start drag
      const eventIsFurtherPropagated = engine.handleDragEvent(
        List([oldEndNodeFeature]),
        [2.1, 2.1]
      );

      // assert - drag started
      expect(eventIsFurtherPropagated).toBeFalsy();

      setup.markerLayer.expectFlagCount(2);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
      setup.markerLayer.expectEndFlagExists('sinkFlag', [2.1, 2.1]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([2, 2]);
      setup.elasticBand.expectAnchor2([2, 2]);
      setup.elasticBand.expectPosition([2.1, 2.1]);

      // act - drag ongoing
      const eventIsFurtherPropagated2 = engine.handleDragEvent(
        List(),
        [2.5, 2.5]
      );

      // assert - drag ongoing
      expect(eventIsFurtherPropagated2).toBeFalsy();

      setup.markerLayer.expectFlagCount(2);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
      setup.markerLayer.expectEndFlagExists('sinkFlag', [2.5, 2.5]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([2, 2]);
      setup.elasticBand.expectAnchor2([2, 2]);
      setup.elasticBand.expectPosition([2.5, 2.5]);

      // act - drag end
      const eventIsFurtherPropagated3 = engine.handleUpEvent(
        List([newEndNodeFeature]),
        [3.1, 3.1]
      );

      // assert - drag end
      expect(eventIsFurtherPropagated3).toBeFalsy();

      const newPlan = setup.context.plan;
      expect(newPlan.legs.size).toEqual(1);
      const newLeg = newPlan.legs.get(0);
      expect(newLeg.source.node.nodeId).toEqual(1001);
      expect(newLeg.sink.node.nodeId).toEqual(1003);

      setup.markerLayer.expectFlagCount(2);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
      setup.markerLayer.expectEndFlagCoordinateExists([3, 3]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(1);
      expect(setup.context.commandStack.last()).toEqual(
        expect.any(PlannerCommandReplaceLeg)
      );
    });

    it('should cancel "move end-node" upon mouse up, while not hoovering over network node', () => {
      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      const oldPlan = setup.createOneLegPlan();
      const oldEndNodeFeature = FlagFeature.end(
        oldPlan.legs.last(null).sinkFlag.featureId
      );

      // act - start drag
      const eventIsFurtherPropagated = engine.handleDragEvent(
        List([oldEndNodeFeature]),
        [2.1, 2.1]
      );

      // assert - drag started
      expect(eventIsFurtherPropagated).toBeFalsy();

      setup.markerLayer.expectFlagCount(2);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
      setup.markerLayer.expectEndFlagExists('sinkFlag', [2.1, 2.1]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([2, 2]);
      setup.elasticBand.expectAnchor2([2, 2]);
      setup.elasticBand.expectPosition([2.1, 2.1]);

      // act - drag end
      const eventIsFurtherPropagated3 = engine.handleUpEvent(
        List(),
        [2.5, 2.5]
      );

      // assert - drag end
      expect(eventIsFurtherPropagated3).toBeFalsy();

      const newPlan = setup.context.plan;
      expect(PlanUtil.planSinkNode(newPlan).nodeId).toEqual('1002');
      expect(newPlan.legs.size).toEqual(1);
      const newLeg = newPlan.legs.get(0);
      expect(newLeg.sourceNode.nodeId).toEqual('1001');
      expect(newLeg.sinkNode.nodeId).toEqual('1002');
      expectCoordinates(newLeg, [1, 1], [2, 2]);

      setup.markerLayer.expectFlagCount(2);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
      setup.markerLayer.expectEndFlagExists('sinkFlag', [2, 2]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(0);
    });
  });

  describe('move via-node', () => {
    it('move via-node', () => {
      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      setup.createTwoLegPlan();
      const newViaNodeFeature = NetworkNodeFeature.create(
        '1004',
        '04',
        null,
        [4, 4],
        false
      );
      const oldViaNodeFeature = FlagFeature.via('sinkFlag1');

      setup.createPlanLegData(setup.node1, setup.node4);
      setup.createPlanLegData(setup.node4, setup.node3);

      // act - start drag
      const eventIsFurtherPropagated = engine.handleDragEvent(
        List([oldViaNodeFeature]),
        [2.1, 2.1]
      );

      // assert - drag started
      expect(eventIsFurtherPropagated).toBeFalsy();

      setup.markerLayer.expectFlagCount(3);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
      setup.markerLayer.expectViaFlagExists('sinkFlag1', [2.1, 2.1]);
      setup.markerLayer.expectEndFlagExists('sinkFlag2', [3, 3]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([3, 3]);
      setup.elasticBand.expectPosition([2.1, 2.1]);

      // act - drag ongoing
      const eventIsFurtherPropagated2 = engine.handleDragEvent(
        List(),
        [2.5, 2.5]
      );

      // assert - drag ongoing
      expect(eventIsFurtherPropagated2).toBeFalsy();

      setup.markerLayer.expectFlagCount(3);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
      setup.markerLayer.expectViaFlagExists('sinkFlag1', [2.5, 2.5]);
      setup.markerLayer.expectEndFlagExists('sinkFlag2', [3, 3]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([3, 3]);
      setup.elasticBand.expectPosition([2.5, 2.5]);

      // act - drag end
      const eventIsFurtherPropagated3 = engine.handleUpEvent(
        List([newViaNodeFeature]),
        [4.1, 4.1]
      );

      // assert - drag end
      expect(eventIsFurtherPropagated3).toBeFalsy();

      const newPlan = setup.context.plan;
      expect(newPlan.legs.size).toEqual(2);
      expect(newPlan.sourceNode.nodeId).toEqual('1001');
      expectStartFlag(newPlan.sourceFlag, 'sourceFlag', [1, 1]);

      setup.markerLayer.expectFlagCount(3);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
      setup.markerLayer.expectViaFlagCoordinateExists([4, 4]);
      setup.markerLayer.expectEndFlagExists('sinkFlag2', [3, 3]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(1);
      expect(setup.context.commandStack.last()).toEqual(
        expect.any(PlannerCommandMoveViaPoint)
      );
    });

    it('should cancel "move via-node" upon mouse up, when not hoovering over network node', () => {
      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      setup.createTwoLegPlan();

      const oldViaNodeFeature = FlagFeature.via('sinkFlag1');

      // act - start drag
      const eventIsFurtherPropagated = engine.handleDragEvent(
        List([oldViaNodeFeature]),
        [2.1, 2.1]
      );

      // assert - drag started
      expect(eventIsFurtherPropagated).toBeFalsy();

      setup.markerLayer.expectFlagCount(3);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
      setup.markerLayer.expectViaFlagExists('sinkFlag1', [2.1, 2.1]);
      setup.markerLayer.expectEndFlagExists('sinkFlag2', [3, 3]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([3, 3]);
      setup.elasticBand.expectPosition([2.1, 2.1]);

      // act - drag end
      const eventIsFurtherPropagated3 = engine.handleUpEvent(
        List(),
        [2.5, 2.5]
      );

      // assert - drag end
      expect(eventIsFurtherPropagated3).toBeFalsy();

      const newPlan = setup.context.plan;
      expect(newPlan.legs.size).toEqual(2);
      expect(newPlan.sourceNode.nodeId).toEqual('1001');
      expectStartFlag(newPlan.sourceFlag, 'sourceFlag', [1, 1]);

      setup.markerLayer.expectFlagCount(3);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
      setup.markerLayer.expectViaFlagExists('sinkFlag1', [2, 2]);
      setup.markerLayer.expectEndFlagExists('sinkFlag2', [3, 3]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(0);
    });
  });

  describe('split leg', () => {
    it('split leg', () => {
      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      const oldPlan = setup.createOneLegPlan();
      const newViaNodeFeature = NetworkNodeFeature.create(
        '1003',
        '03',
        null,
        [3, 3],
        false
      );
      const oldLegFeature = new LegFeature(oldPlan.legs.get(0).featureId);

      setup.createPlanLegData(setup.node1, setup.node3);
      setup.createPlanLegData(setup.node3, setup.node2);

      // act - start drag
      const eventIsFurtherPropagated = engine.handleDragEvent(
        List([oldLegFeature]),
        [1.5, 1.5]
      );

      // assert - drag started
      expect(eventIsFurtherPropagated).toBeFalsy();

      setup.markerLayer.expectFlagCount(2);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
      setup.markerLayer.expectEndFlagExists('sinkFlag', [2, 2]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([2, 2]);
      setup.elasticBand.expectPosition([1.5, 1.5]);

      // act - drag ongoing
      const eventIsFurtherPropagated2 = engine.handleDragEvent(
        List(),
        [1.7, 1.7]
      );

      // assert - drag ongoing
      expect(eventIsFurtherPropagated2).toBeFalsy();

      setup.markerLayer.expectFlagCount(2);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
      setup.markerLayer.expectEndFlagExists('sinkFlag', [2, 2]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([2, 2]);
      setup.elasticBand.expectPosition([1.7, 1.7]);

      // act - drag end
      const eventIsFurtherPropagated3 = engine.handleUpEvent(
        List([newViaNodeFeature]),
        [3.1, 3.1]
      );

      // assert - drag end
      expect(eventIsFurtherPropagated3).toBeFalsy();

      const newPlan = setup.context.plan;
      expect(newPlan.legs.size).toEqual(2);
      expect(newPlan.sourceNode.nodeId).toEqual('1001');

      setup.markerLayer.expectFlagCount(3);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
      setup.markerLayer.expectViaFlagCoordinateExists([3, 3]);
      setup.markerLayer.expectEndFlagCoordinateExists([2, 2]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(1);
      const command = setup.context.commandStack.last();
      expect(command).toEqual(expect.any(PlannerCommandSplitLeg));
    });

    it('should cancel "split leg" upon mouse up, while not hoovering over network node', () => {
      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      const oldPlan = setup.createOneLegPlan();

      const oldLegFeature = new LegFeature(oldPlan.legs.get(0).featureId);

      // act - start drag
      const eventIsFurtherPropagated = engine.handleDragEvent(
        List([oldLegFeature]),
        [1.5, 1.5]
      );

      // assert - drag started
      expect(eventIsFurtherPropagated).toBeFalsy();

      setup.markerLayer.expectFlagCount(2);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
      setup.markerLayer.expectEndFlagExists('sinkFlag', [2, 2]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([2, 2]);
      setup.elasticBand.expectPosition([1.5, 1.5]);

      // act - drag end
      const eventIsFurtherPropagated3 = engine.handleUpEvent(
        List(),
        [1.7, 1.7]
      );

      // assert - drag end
      expect(eventIsFurtherPropagated3).toBeFalsy();

      const newPlan = setup.context.plan;
      expect(newPlan.legs.size).toEqual(1);

      setup.markerLayer.expectFlagCount(2);
      setup.markerLayer.expectStartFlagExists('sourceFlag', [1, 1]);
      setup.markerLayer.expectEndFlagExists('sinkFlag', [2, 2]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(0);
    });
  });
});
