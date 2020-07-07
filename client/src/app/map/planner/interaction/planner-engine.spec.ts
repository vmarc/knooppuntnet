import {List} from "immutable";
import {TestSupport} from "../../../util/test-support";
import {PlannerCommandMoveEndPoint} from "../commands/planner-command-move-end-point";
import {PlannerCommandMoveFirstLegSource} from "../commands/planner-command-move-first-leg-source";
import {PlannerCommandMoveStartPoint} from "../commands/planner-command-move-start-point";
import {PlannerCommandMoveViaPoint} from "../commands/planner-command-move-via-point";
import {PlannerCommandSplitLeg} from "../commands/planner-command-split-leg";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {FlagFeature} from "../features/flag-feature";
import {LegFeature} from "../features/leg-feature";
import {MapFeature} from "../features/map-feature";
import {NetworkNodeFeature} from "../features/network-node-feature";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanUtil} from "../plan/plan-util";
import {PlannerEngineImpl} from "./planner-engine-impl";

describe("PlannerEngine", () => {

  describe("add start node", () => {

    it("should add start node upon handle down event while hoovering over network node", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);

      const node = NetworkNodeFeature.create("1001", "01", [1, 1]);
      const features: List<MapFeature> = List([node]);

      // act
      const eventIsNotFurtherPropagated = engine.handleDownEvent(features, [1.1, 1.1], false);

      // assert
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");
      expect(setup.context.plan.legs.size).toEqual(0);

      setup.routeLayer.expectFlagCount(1);
      setup.routeLayer.expectStartFlagExists(setup.context.plan.sourceNode.featureId, [1, 1]);

      expect(setup.context.commandStack.commandCount).toEqual(1);
    });

  });

  describe("add leg when plan already has start point", () => {

    it("should add new leg upon handle down event while hoovering over network node", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);

      const node1 = NetworkNodeFeature.create("1001", "01", [1, 1]);
      const node2 = NetworkNodeFeature.create("1002", "02", [2, 2]);

      setup.context.updatePlan(new Plan(node1.node, PlanFlag.start("n1", [1, 1]), List()));

      const features: List<MapFeature> = List([node2]);

      // act
      const eventIsNotFurtherPropagated = engine.handleDownEvent(features, [2.1, 2.1], false);

      // assert
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");
      expect(setup.context.plan.legs.size).toEqual(1);
      expect(setup.context.plan.legs.get(0).sourceNode.nodeId).toEqual("1001");
      expect(setup.context.plan.legs.get(0).sinkNode.nodeId).toEqual("1002");

      setup.routeLayer.expectFlagCount(1);
      setup.routeLayer.expectEndFlagExists(setup.context.plan.legs.get(0).sinkNode.featureId, [2, 2]);

      expect(setup.context.commandStack.commandCount).toEqual(1);
    });

  });

  describe("move start-node", () => {

    it("should handle start node drag, while no legs in plan yet", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);

      const plan = setup.createPlanWithStartPointOnly();

      const oldStartNodeFeature = FlagFeature.start(plan.sourceNode.featureId);
      const newStartNodeFeature = NetworkNodeFeature.create("1002", "02", [2, 2]);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([oldStartNodeFeature]), [1.1, 1.1], false);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.routeLayer.expectFlagCount(1);
      setup.routeLayer.expectStartFlagExists(oldStartNodeFeature.id, [1.1, 1.1]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.1, 1.1]);

      // act - drag ongoing
      const eventIsNotFurtherPropagated2 = engine.handleDragEvent(List(), [1.5, 1.5], false);

      // assert - drag ongoing
      expect(eventIsNotFurtherPropagated2).toBeTruthy();

      setup.routeLayer.expectFlagCount(1);
      setup.routeLayer.expectStartFlagExists(oldStartNodeFeature.id, [1.5, 1.5]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.5, 1.5]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List([newStartNodeFeature]), [2.1, 2.1], false, false);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeTruthy();

      expect(setup.context.plan.sourceNode.nodeId).toEqual("1002");
      expect(setup.context.plan.legs.size).toEqual(0);

      setup.routeLayer.expectFlagCount(1);
      setup.routeLayer.expectStartFlagExists(setup.context.plan.sourceNode.featureId, [2, 2]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(1);
      const command = setup.context.commandStack.last();
      expect(command).toEqual(jasmine.any(PlannerCommandMoveStartPoint));
    });

    it("should cancel 'move start-node' upon mouse up, while no legs in plan yet, and not hoovering over network node", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);

      const plan = setup.createPlanWithStartPointOnly();

      const startFlag = FlagFeature.start(plan.sourceNode.featureId);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([startFlag]), [1.1, 1.1], false);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.routeLayer.expectFlagCount(1);
      setup.routeLayer.expectStartFlagExists(startFlag.id, [1.1, 1.1]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.1, 1.1]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List(), [1.5, 1.5], false, false);

      // assert - drag cancelled
      expect(eventIsNotFurtherPropagated3).toBeFalsy();

      expect(setup.context.plan.sourceNode.nodeId).toEqual("1001");
      expect(setup.context.plan.legs.size).toEqual(0);

      setup.routeLayer.expectFlagCount(1);
      setup.routeLayer.expectStartFlagExists(setup.context.plan.sourceNode.featureId, [1, 1]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(0);
    });

    it("should handle start node drag, while legs in plan", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      const oldPlan = setup.createOneLegPlan();
      const newStartNodeFeature = NetworkNodeFeature.create("1003", "03", [3, 3]);
      setup.createLeg(setup.node3, setup.node2);
      const oldStartNodeFeature = FlagFeature.start(oldPlan.sourceNode.featureId);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([oldStartNodeFeature]), [1.1, 1.1], false);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectStartFlagExists(oldPlan.sourceNode.featureId, [1.1, 1.1]);
      setup.routeLayer.expectViaFlagExists(oldPlan.legs.get(0).sinkNode.featureId, [2, 2]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.1, 1.1]);

      // act - drag ongoing
      const eventIsNotFurtherPropagated2 = engine.handleDragEvent(List(), [1.5, 1.5], false);

      // assert - drag ongoing
      expect(eventIsNotFurtherPropagated2).toBeTruthy();

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectStartFlagExists(oldStartNodeFeature.id, [1.5, 1.5]);
      setup.routeLayer.expectViaFlagExists(oldPlan.legs.get(0).sinkNode.featureId, [2, 2]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.5, 1.5]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List([newStartNodeFeature]), [3.1, 3.1], false, false);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeTruthy();

      const newPlan = setup.context.plan;
      expect(newPlan.sourceNode.nodeId).toEqual("1003");
      expect(newPlan.legs.size).toEqual(1);
      const newLeg = newPlan.legs.get(0);
      expect(newLeg.sourceNode.nodeId).toEqual("1003");
      expect(newLeg.sinkNode.nodeId).toEqual("1002");
      TestSupport.expectCoordinates(newLeg, [3, 3], [2, 2]);

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectStartFlagExists(newPlan.sourceNode.featureId, [3, 3]);
      setup.routeLayer.expectViaFlagExists(newLeg.sinkNode.featureId, [2, 2]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(1);
      expect(setup.context.commandStack.last()).toEqual(jasmine.any(PlannerCommandMoveFirstLegSource));

    });

    it("should cancel 'move start-node' upon mouse up, with legs in plan, and not hoovering over network node", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      const oldPlan = setup.createOneLegPlan();

      const oldStartNodeFeature = FlagFeature.start(oldPlan.sourceNode.featureId);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([oldStartNodeFeature]), [1.1, 1.1], false);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectStartFlagExists(oldPlan.sourceNode.featureId, [1.1, 1.1]);
      setup.routeLayer.expectViaFlagExists(oldPlan.legs.get(0).sinkNode.featureId, [2, 2]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.1, 1.1]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List(), [1.5, 1.5], false, false);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeFalsy();

      const newPlan = setup.context.plan;
      expect(newPlan.sourceNode.nodeId).toEqual("1001");
      expect(newPlan.legs.size).toEqual(1);
      const newLeg = newPlan.legs.get(0);
      expect(newLeg.sourceNode.nodeId).toEqual("1001");
      expect(newLeg.sinkNode.nodeId).toEqual("1002");

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectStartFlagExists(newPlan.sourceNode.featureId, [1, 1]);
      setup.routeLayer.expectViaFlagExists(newLeg.sinkNode.featureId, [2, 2]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(0);

    });
  });

  describe("move end-node", () => {

    it("move end-node", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      const oldPlan = setup.createOneLegPlan();
      const newEndNodeFeature = NetworkNodeFeature.create("1003", "03", [3, 3]);
      setup.createLeg(setup.node1, setup.node3);
      const oldEndNodeFeature = FlagFeature.via(PlanUtil.planSinkNode(oldPlan).featureId);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([oldEndNodeFeature]), [2.1, 2.1], false);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectStartFlagExists(oldPlan.sourceNode.featureId, [1, 1]);
      setup.routeLayer.expectViaFlagExists(PlanUtil.planSinkNode(oldPlan).featureId, [2.1, 2.1]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([2, 2]);
      setup.elasticBand.expectAnchor2([2, 2]);
      setup.elasticBand.expectPosition([2.1, 2.1]);

      // act - drag ongoing
      const eventIsNotFurtherPropagated2 = engine.handleDragEvent(List(), [2.5, 2.5], false);

      // assert - drag ongoing
      expect(eventIsNotFurtherPropagated2).toBeTruthy();

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectStartFlagExists(oldPlan.sourceNode.featureId, [1, 1]);
      setup.routeLayer.expectViaFlagExists(PlanUtil.planSinkNode(oldPlan).featureId, [2.5, 2.5]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([2, 2]);
      setup.elasticBand.expectAnchor2([2, 2]);
      setup.elasticBand.expectPosition([2.5, 2.5]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List([newEndNodeFeature]), [3.1, 3.1], false, false);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeTruthy();

      const newPlan = setup.context.plan;
      expect(PlanUtil.planSinkNode(newPlan).nodeId).toEqual("1003");
      expect(newPlan.legs.size).toEqual(1);
      const newLeg = newPlan.legs.get(0);
      expect(newLeg.sourceNode.nodeId).toEqual("1001");
      expect(newLeg.sinkNode.nodeId).toEqual("1003");
      TestSupport.expectCoordinates(newLeg, [1, 1], [3, 3]);

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectStartFlagExists(newPlan.sourceNode.featureId, [1, 1]);
      setup.routeLayer.expectEndFlagExists(newLeg.sinkNode.featureId, [3, 3]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(1);
      expect(setup.context.commandStack.last()).toEqual(jasmine.any(PlannerCommandMoveEndPoint));

    });

    it("should cancel 'move end-node' upon mouse up, while not hoovering over network node", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      const oldPlan = setup.createOneLegPlan();
      const oldEndNodeFeature = FlagFeature.via(PlanUtil.planSinkNode(oldPlan).featureId);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([oldEndNodeFeature]), [2.1, 2.1], false);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectStartFlagExists(oldPlan.sourceNode.featureId, [1, 1]);
      setup.routeLayer.expectViaFlagExists(PlanUtil.planSinkNode(oldPlan).featureId, [2.1, 2.1]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([2, 2]);
      setup.elasticBand.expectAnchor2([2, 2]);
      setup.elasticBand.expectPosition([2.1, 2.1]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List(), [2.5, 2.5], false, false);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeFalsy();

      const newPlan = setup.context.plan;
      expect(PlanUtil.planSinkNode(newPlan).nodeId).toEqual("1002");
      expect(newPlan.legs.size).toEqual(1);
      const newLeg = newPlan.legs.get(0);
      expect(newLeg.sourceNode.nodeId).toEqual("1001");
      expect(newLeg.sinkNode.nodeId).toEqual("1002");
      TestSupport.expectCoordinates(newLeg, [1, 1], [2, 2]);

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectStartFlagExists(newPlan.sourceNode.featureId, [1, 1]);
      setup.routeLayer.expectViaFlagExists(newLeg.sinkNode.featureId, [2, 2]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(0);

    });

  });

  describe("move via-node", () => {

    it("move via-node", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      const oldPlan = setup.createTwoLegPlan();
      const newViaNodeFeature = NetworkNodeFeature.create("1004", "04", [4, 4]);
      setup.createLeg(setup.node1, setup.node4);
      setup.createLeg(setup.node4, setup.node3);
      const oldViaNodeFeature = FlagFeature.via(oldPlan.legs.get(0).sinkNode.featureId);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([oldViaNodeFeature]), [2.1, 2.1], false);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.routeLayer.expectFlagCount(3);
      setup.routeLayer.expectStartFlagExists(oldPlan.sourceNode.featureId, [1, 1]);
      setup.routeLayer.expectViaFlagExists(PlanUtil.planSinkNode(oldPlan).featureId, [3, 3]);
      setup.routeLayer.expectViaFlagExists(oldViaNodeFeature.id, [2.1, 2.1]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([3, 3]);
      setup.elasticBand.expectPosition([2.1, 2.1]);

      // act - drag ongoing
      const eventIsNotFurtherPropagated2 = engine.handleDragEvent(List(), [2.5, 2.5], false);

      // assert - drag ongoing
      expect(eventIsNotFurtherPropagated2).toBeTruthy();

      setup.routeLayer.expectFlagCount(3);
      setup.routeLayer.expectStartFlagExists(oldPlan.sourceNode.featureId, [1, 1]);
      setup.routeLayer.expectViaFlagExists(PlanUtil.planSinkNode(oldPlan).featureId, [3, 3]);
      setup.routeLayer.expectViaFlagExists(oldViaNodeFeature.id, [2.5, 2.5]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([3, 3]);
      setup.elasticBand.expectPosition([2.5, 2.5]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List([newViaNodeFeature]), [4.1, 4.1], false, false);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeTruthy();

      const newPlan = setup.context.plan;
      expect(newPlan.legs.size).toEqual(2);
      const newLeg1 = newPlan.legs.get(0);
      const newLeg2 = newPlan.legs.get(1);
      expect(newPlan.sourceNode.nodeId).toEqual("1001");
      expect(PlanUtil.planSinkNode(newPlan).nodeId).toEqual("1003");

      expect(newLeg1.sourceNode.nodeId).toEqual("1001");
      expect(newLeg1.sinkNode.nodeId).toEqual("1004");

      expect(newLeg2.sourceNode.nodeId).toEqual("1004");
      expect(newLeg2.sinkNode.nodeId).toEqual("1003");

      TestSupport.expectCoordinates(newLeg1, [1, 1], [4, 4]);
      TestSupport.expectCoordinates(newLeg2, [4, 4], [3, 3]);

      setup.routeLayer.expectFlagCount(3);
      setup.routeLayer.expectStartFlagExists(newPlan.sourceNode.featureId, [1, 1]);
      setup.routeLayer.expectViaFlagExists(PlanUtil.planSinkNode(newPlan).featureId, [3, 3]);
      setup.routeLayer.expectViaFlagExists(newPlan.legs.get(0).sinkNode.featureId, [4, 4]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(1);
      expect(setup.context.commandStack.last()).toEqual(jasmine.any(PlannerCommandMoveViaPoint));

    });

    it("should cancel 'move via-node' upon mouse up, when not hoovering over network node", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      const oldPlan = setup.createTwoLegPlan();

      const oldViaNodeFeature = FlagFeature.via(oldPlan.legs.get(0).sinkNode.featureId);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([oldViaNodeFeature]), [2.1, 2.1], false);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.routeLayer.expectFlagCount(3);
      setup.routeLayer.expectStartFlagExists(oldPlan.sourceNode.featureId, [1, 1]);
      setup.routeLayer.expectViaFlagExists(PlanUtil.planSinkNode(oldPlan).featureId, [3, 3]);
      setup.routeLayer.expectViaFlagExists(oldViaNodeFeature.id, [2.1, 2.1]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([3, 3]);
      setup.elasticBand.expectPosition([2.1, 2.1]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List(), [2.5, 2.5], false, false);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeFalsy();

      const newPlan = setup.context.plan;
      expect(newPlan.legs.size).toEqual(2);
      const newLeg1 = newPlan.legs.get(0);
      const newLeg2 = newPlan.legs.get(1);
      expect(newPlan.sourceNode.nodeId).toEqual("1001");
      expect(PlanUtil.planSinkNode(newPlan).nodeId).toEqual("1003");

      expect(newLeg1.sourceNode.nodeId).toEqual("1001");
      expect(newLeg1.sinkNode.nodeId).toEqual("1002");

      expect(newLeg2.sourceNode.nodeId).toEqual("1002");
      expect(newLeg2.sinkNode.nodeId).toEqual("1003");

      TestSupport.expectCoordinates(newLeg1, [1, 1], [2, 2]);
      TestSupport.expectCoordinates(newLeg2, [2, 2], [3, 3]);

      setup.routeLayer.expectFlagCount(3);
      setup.routeLayer.expectStartFlagExists(newPlan.sourceNode.featureId, [1, 1]);
      setup.routeLayer.expectViaFlagExists(PlanUtil.planSinkNode(newPlan).featureId, [3, 3]);
      setup.routeLayer.expectViaFlagExists(newPlan.legs.get(0).sinkNode.featureId, [2, 2]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(0);

    });

  });

  describe("split leg", () => {

    it("split leg", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      const oldPlan = setup.createOneLegPlan();
      const newViaNodeFeature = NetworkNodeFeature.create("1003", "03", [3, 3]);
      setup.createLeg(setup.node1, setup.node3);
      setup.createLeg(setup.node3, setup.node2);
      const oldLegFeature = new LegFeature(oldPlan.legs.get(0).featureId);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([oldLegFeature]), [1.5, 1.5], false);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectStartFlagExists(oldPlan.sourceNode.featureId, [1, 1]);
      setup.routeLayer.expectViaFlagExists(PlanUtil.planSinkNode(oldPlan).featureId, [2, 2]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([2, 2]);
      setup.elasticBand.expectPosition([1.5, 1.5]);

      // act - drag ongoing
      const eventIsNotFurtherPropagated2 = engine.handleDragEvent(List(), [1.7, 1.7], false);

      // assert - drag ongoing
      expect(eventIsNotFurtherPropagated2).toBeTruthy();

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectStartFlagExists(oldPlan.sourceNode.featureId, [1, 1]);
      setup.routeLayer.expectViaFlagExists(PlanUtil.planSinkNode(oldPlan).featureId, [2, 2]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([2, 2]);
      setup.elasticBand.expectPosition([1.7, 1.7]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List([newViaNodeFeature]), [3.1, 3.1], false, false);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeTruthy();

      const newPlan = setup.context.plan;
      expect(newPlan.legs.size).toEqual(2);
      const newLeg1 = newPlan.legs.get(0);
      const newLeg2 = newPlan.legs.get(1);
      expect(newPlan.sourceNode.nodeId).toEqual("1001");
      expect(PlanUtil.planSinkNode(newPlan).nodeId).toEqual("1002");

      expect(newLeg1.sourceNode.nodeId).toEqual("1001");
      expect(newLeg1.sinkNode.nodeId).toEqual("1003");

      expect(newLeg2.sourceNode.nodeId).toEqual("1003");
      expect(newLeg2.sinkNode.nodeId).toEqual("1002");

      TestSupport.expectCoordinates(newLeg1, [1, 1], [3, 3]);
      TestSupport.expectCoordinates(newLeg2, [3, 3], [2, 2]);

      setup.routeLayer.expectFlagCount(3);
      setup.routeLayer.expectStartFlagExists(newPlan.sourceNode.featureId, [1, 1]);
      setup.routeLayer.expectViaFlagExists(PlanUtil.planSinkNode(newPlan).featureId, [2, 2]);
      setup.routeLayer.expectViaFlagExists(newPlan.legs.get(0).sinkNode.featureId, [3, 3]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(1);
      const command = setup.context.commandStack.last();
      expect(command).toEqual(jasmine.any(PlannerCommandSplitLeg));

    });

    it("should cancel 'split leg' upon mouse up, while not hoovering over network node", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      const oldPlan = setup.createOneLegPlan();

      const oldLegFeature = new LegFeature(oldPlan.legs.get(0).featureId);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([oldLegFeature]), [1.5, 1.5], false);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectStartFlagExists(oldPlan.sourceNode.featureId, [1, 1]);
      setup.routeLayer.expectViaFlagExists(PlanUtil.planSinkNode(oldPlan).featureId, [2, 2]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([2, 2]);
      setup.elasticBand.expectPosition([1.5, 1.5]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List(), [1.7, 1.7], false, false);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeFalsy();

      const newPlan = setup.context.plan;
      expect(newPlan.legs.size).toEqual(1);
      const newLeg = newPlan.legs.get(0);
      expect(newPlan.sourceNode.nodeId).toEqual("1001");
      expect(PlanUtil.planSinkNode(newPlan).nodeId).toEqual("1002");

      expect(newLeg.sourceNode.nodeId).toEqual("1001");
      expect(newLeg.sinkNode.nodeId).toEqual("1002");

      TestSupport.expectCoordinates(newLeg, [1, 1], [2, 2]);

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectStartFlagExists(newPlan.sourceNode.featureId, [1, 1]);
      setup.routeLayer.expectViaFlagExists(PlanUtil.planSinkNode(newPlan).featureId, [2, 2]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(0);

    });

  });

});
