import {List} from "immutable";
import {TestSupport} from "../../../util/test-support";
import {PlannerCommandMoveEndPoint} from "../commands/planner-command-move-end-point";
import {PlannerCommandMoveFirstLegSource} from "../commands/planner-command-move-first-leg-source";
import {PlannerCommandMoveStartPoint} from "../commands/planner-command-move-start-point";
import {PlannerCommandMoveViaPoint} from "../commands/planner-command-move-via-point";
import {PlannerCommandSplitLeg} from "../commands/planner-command-split-leg";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlannerMapFeature} from "../features/planner-map-feature";
import {Plan} from "../plan/plan";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlannerEngineImpl} from "./planner-engine-impl";

describe("PlannerEngine", () => {

  describe("crosshair", () => {

    it("should update crosshair position upon down event, and no features below cursor", () => {

    });

    it("should update crosshair position upon move event", () => {

    });

    it("should hide crosshair during drag operations", () => {

    });

    it("should make crosshair invisible upon mouse out", () => {
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      engine.handleMouseOut();
      setup.crosshair.expectVisible(false);
    });

    it("should make crosshair visible again upon mouse enter", () => {
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      engine.handleMouseEnter();
      setup.crosshair.expectVisible(true);
    });

  });

  describe("add start node", () => {

    it("should add start node upon handle down event while hoovering over network node", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);

      const node = PlannerMapFeature.networkNode("1001", "01", [1, 1]);
      const features: List<PlannerMapFeature> = List([node]);

      // act
      const eventIsNotFurtherPropagated = engine.handleDownEvent(features, [1.1, 1.1]);

      // assert
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      expect(setup.context.plan.source.nodeId).toEqual("1001");
      expect(setup.context.plan.legs.size).toEqual(0);

      setup.crosshair.expectVisible(true);
      setup.crosshair.expectPosition([1, 1]); // snapped to network node coordinate, not cursor position

      setup.routeLayer.expectFlagCount(1);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, setup.context.plan.source.featureId, [1, 1]);

      expect(setup.context.commandStack.commandCount).toEqual(1);
    });

  });

  describe("add leg when plan already has start point", () => {

    it("should add new leg upon handle down event while hoovering over network node", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);

      const node1 = PlannerMapFeature.networkNode("1001", "01", [1, 1]);
      const node2 = PlannerMapFeature.networkNode("1002", "02", [2, 2]);

      setup.context.updatePlan(Plan.create(node1.node, List()));

      const features: List<PlannerMapFeature> = List([node2]);

      // act
      const eventIsNotFurtherPropagated = engine.handleDownEvent(features, [2.1, 2.1]);

      // assert
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.crosshair.expectVisible(true);
      setup.crosshair.expectPosition([2, 2]); // snapped to network node coordinate, not cursor position

      expect(setup.context.plan.source.nodeId).toEqual("1001");
      expect(setup.context.plan.legs.size).toEqual(1);
      expect(setup.context.plan.legs.get(0).source.nodeId).toEqual("1001");
      expect(setup.context.plan.legs.get(0).sink.nodeId).toEqual("1002");

      setup.routeLayer.expectFlagCount(1);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, setup.context.plan.legs.get(0).sink.featureId, [2, 2]);

      expect(setup.context.commandStack.commandCount).toEqual(1);
    });

  });

  describe("move start-node", () => {

    it("should handle start node drag, while no legs in plan yet", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);

      const plan = setup.createPlanWithStartPointOnly();

      const oldStartNodeFeature = PlannerMapFeature.startFlag(plan.source.featureId);
      const newStartNodeFeature = PlannerMapFeature.networkNode("1002", "02", [2, 2]);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([oldStartNodeFeature]), [1.1, 1.1]);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.crosshair.expectVisible(false);

      setup.routeLayer.expectFlagCount(1);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, oldStartNodeFeature.featureId, [1.1, 1.1]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.1, 1.1]);

      // act - drag ongoing
      const eventIsNotFurtherPropagated2 = engine.handleDragEvent(List(), [1.5, 1.5]);

      // assert - drag ongoing
      expect(eventIsNotFurtherPropagated2).toBeTruthy();

      setup.crosshair.expectVisible(false);

      setup.routeLayer.expectFlagCount(1);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, oldStartNodeFeature.featureId, [1.5, 1.5]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.5, 1.5]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List([newStartNodeFeature]), [2.1, 2.1]);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeTruthy();

      expect(setup.context.plan.source.nodeId).toEqual("1002");
      expect(setup.context.plan.legs.size).toEqual(0);

      setup.crosshair.expectVisible(true);
      setup.crosshair.expectPosition([2.1, 2.1]); // no snap

      setup.routeLayer.expectFlagCount(1);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, setup.context.plan.source.featureId, [2, 2]);

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

      const startFlag = PlannerMapFeature.startFlag(plan.source.featureId);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([startFlag]), [1.1, 1.1]);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.crosshair.expectVisible(false);

      setup.routeLayer.expectFlagCount(1);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, startFlag.featureId, [1.1, 1.1]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.1, 1.1]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List(), [1.5, 1.5]);

      // assert - drag cancelled
      expect(eventIsNotFurtherPropagated3).toBeFalsy();

      expect(setup.context.plan.source.nodeId).toEqual("1001");
      expect(setup.context.plan.legs.size).toEqual(0);

      setup.crosshair.expectVisible(true);
      setup.crosshair.expectPosition([1.5, 1.5]);

      setup.routeLayer.expectFlagCount(1);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, setup.context.plan.source.featureId, [1, 1]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(0);
    });

    it("should handle start node drag, while legs in plan", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      const oldPlan = setup.createOneLegPlan();
      const newStartNodeFeature = PlannerMapFeature.networkNode("1003", "03", [3, 3]);
      setup.createLeg(setup.node3, setup.node2);
      const oldStartNodeFeature = PlannerMapFeature.startFlag(oldPlan.source.featureId);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([oldStartNodeFeature]), [1.1, 1.1]);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.crosshair.expectVisible(false);

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, oldPlan.source.featureId, [1.1, 1.1]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, oldPlan.legs.get(0).sink.featureId, [2, 2]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.1, 1.1]);

      // act - drag ongoing
      const eventIsNotFurtherPropagated2 = engine.handleDragEvent(List(), [1.5, 1.5]);

      // assert - drag ongoing
      expect(eventIsNotFurtherPropagated2).toBeTruthy();

      setup.crosshair.expectVisible(false);

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, oldStartNodeFeature.featureId, [1.5, 1.5]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, oldPlan.legs.get(0).sink.featureId, [2, 2]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.5, 1.5]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List([newStartNodeFeature]), [3.1, 3.1]);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeTruthy();

      const newPlan = setup.context.plan;
      expect(newPlan.source.nodeId).toEqual("1003");
      expect(newPlan.legs.size).toEqual(1);
      const newLeg = newPlan.legs.get(0);
      expect(newLeg.source.nodeId).toEqual("1003");
      expect(newLeg.sink.nodeId).toEqual("1002");
      TestSupport.expectCoordinates(newLeg.coordinates(), [3, 3], [2, 2]);

      setup.crosshair.expectVisible(true);
      setup.crosshair.expectPosition([3.1, 3.1]); // no snap

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, newPlan.source.featureId, [3, 3]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, newLeg.sink.featureId, [2, 2]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(1);
      expect(setup.context.commandStack.last()).toEqual(jasmine.any(PlannerCommandMoveFirstLegSource));

    });

    it("should cancel 'move start-node' upon mouse up, with legs in plan, and not hoovering over network node", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      const oldPlan = setup.createOneLegPlan();

      const oldStartNodeFeature = PlannerMapFeature.startFlag(oldPlan.source.featureId);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([oldStartNodeFeature]), [1.1, 1.1]);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.crosshair.expectVisible(false);

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, oldPlan.source.featureId, [1.1, 1.1]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, oldPlan.legs.get(0).sink.featureId, [2, 2]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.1, 1.1]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List(), [1.5, 1.5]);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeFalsy();

      const newPlan = setup.context.plan;
      expect(newPlan.source.nodeId).toEqual("1001");
      expect(newPlan.legs.size).toEqual(1);
      const newLeg = newPlan.legs.get(0);
      expect(newLeg.source.nodeId).toEqual("1001");
      expect(newLeg.sink.nodeId).toEqual("1002");

      setup.crosshair.expectVisible(true);
      setup.crosshair.expectPosition([1.5, 1.5]);

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, newPlan.source.featureId, [1, 1]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, newLeg.sink.featureId, [2, 2]);

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
      const newEndNodeFeature = PlannerMapFeature.networkNode("1003", "03", [3, 3]);
      setup.createLeg(setup.node1, setup.node3);
      const oldEndNodeFeature = PlannerMapFeature.viaFlag(oldPlan.sink.featureId);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([oldEndNodeFeature]), [2.1, 2.1]);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.crosshair.expectVisible(false);
      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, oldPlan.source.featureId, [1, 1]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, oldPlan.sink.featureId, [2.1, 2.1]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([2, 2]);
      setup.elasticBand.expectAnchor2([2, 2]);
      setup.elasticBand.expectPosition([2.1, 2.1]);

      // act - drag ongoing
      const eventIsNotFurtherPropagated2 = engine.handleDragEvent(List(), [2.5, 2.5]);

      // assert - drag ongoing
      expect(eventIsNotFurtherPropagated2).toBeTruthy();

      setup.crosshair.expectVisible(false);

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, oldPlan.source.featureId, [1, 1]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, oldPlan.sink.featureId, [2.5, 2.5]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([2, 2]);
      setup.elasticBand.expectAnchor2([2, 2]);
      setup.elasticBand.expectPosition([2.5, 2.5]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List([newEndNodeFeature]), [3.1, 3.1]);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeTruthy();

      const newPlan = setup.context.plan;
      expect(newPlan.sink.nodeId).toEqual("1003");
      expect(newPlan.legs.size).toEqual(1);
      const newLeg = newPlan.legs.get(0);
      expect(newLeg.source.nodeId).toEqual("1001");
      expect(newLeg.sink.nodeId).toEqual("1003");
      TestSupport.expectCoordinates(newLeg.coordinates(), [1, 1], [3, 3]);

      setup.crosshair.expectVisible(true);
      setup.crosshair.expectPosition([3.1, 3.1]); // no snap

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, newPlan.source.featureId, [1, 1]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, newLeg.sink.featureId, [3, 3]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(1);
      expect(setup.context.commandStack.last()).toEqual(jasmine.any(PlannerCommandMoveEndPoint));

    });

    it("should cancel 'move end-node' upon mouse up, while not hoovering over network node", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      const oldPlan = setup.createOneLegPlan();
      const oldEndNodeFeature = PlannerMapFeature.viaFlag(oldPlan.sink.featureId);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([oldEndNodeFeature]), [2.1, 2.1]);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.crosshair.expectVisible(false);
      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, oldPlan.source.featureId, [1, 1]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, oldPlan.sink.featureId, [2.1, 2.1]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([2, 2]);
      setup.elasticBand.expectAnchor2([2, 2]);
      setup.elasticBand.expectPosition([2.1, 2.1]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List(), [2.5, 2.5]);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeFalsy();

      const newPlan = setup.context.plan;
      expect(newPlan.sink.nodeId).toEqual("1002");
      expect(newPlan.legs.size).toEqual(1);
      const newLeg = newPlan.legs.get(0);
      expect(newLeg.source.nodeId).toEqual("1001");
      expect(newLeg.sink.nodeId).toEqual("1002");
      TestSupport.expectCoordinates(newLeg.coordinates(), [1, 1], [2, 2]);

      setup.crosshair.expectVisible(true);
      setup.crosshair.expectPosition([2.5, 2.5]);

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, newPlan.source.featureId, [1, 1]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, newLeg.sink.featureId, [2, 2]);

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
      const newViaNodeFeature = PlannerMapFeature.networkNode("1004", "04", [4, 4]);
      setup.createLeg(setup.node1, setup.node4);
      setup.createLeg(setup.node4, setup.node3);
      const oldViaNodeFeature = PlannerMapFeature.viaFlag(oldPlan.legs.get(0).sink.featureId);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([oldViaNodeFeature]), [2.1, 2.1]);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.crosshair.expectVisible(false);
      setup.routeLayer.expectFlagCount(3);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, oldPlan.source.featureId, [1, 1]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, oldPlan.sink.featureId, [3, 3]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, oldViaNodeFeature.featureId, [2.1, 2.1]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([3, 3]);
      setup.elasticBand.expectPosition([2.1, 2.1]);

      // act - drag ongoing
      const eventIsNotFurtherPropagated2 = engine.handleDragEvent(List(), [2.5, 2.5]);

      // assert - drag ongoing
      expect(eventIsNotFurtherPropagated2).toBeTruthy();

      setup.crosshair.expectVisible(false);

      setup.routeLayer.expectFlagCount(3);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, oldPlan.source.featureId, [1, 1]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, oldPlan.sink.featureId, [3, 3]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, oldViaNodeFeature.featureId, [2.5, 2.5]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([3, 3]);
      setup.elasticBand.expectPosition([2.5, 2.5]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List([newViaNodeFeature]), [4.1, 4.1]);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeTruthy();

      const newPlan = setup.context.plan;
      expect(newPlan.legs.size).toEqual(2);
      const newLeg1 = newPlan.legs.get(0);
      const newLeg2 = newPlan.legs.get(1);
      expect(newPlan.source.nodeId).toEqual("1001");
      expect(newPlan.sink.nodeId).toEqual("1003");

      expect(newLeg1.source.nodeId).toEqual("1001");
      expect(newLeg1.sink.nodeId).toEqual("1004");

      expect(newLeg2.source.nodeId).toEqual("1004");
      expect(newLeg2.sink.nodeId).toEqual("1003");

      TestSupport.expectCoordinates(newLeg1.coordinates(), [1, 1], [4, 4]);
      TestSupport.expectCoordinates(newLeg2.coordinates(), [4, 4], [3, 3]);

      setup.crosshair.expectVisible(true);
      setup.crosshair.expectPosition([4.1, 4.1]); // no snap

      setup.routeLayer.expectFlagCount(3);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, newPlan.source.featureId, [1, 1]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, newPlan.sink.featureId, [3, 3]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, newPlan.legs.get(0).sink.featureId, [4, 4]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(1);
      expect(setup.context.commandStack.last()).toEqual(jasmine.any(PlannerCommandMoveViaPoint));

    });

    it("should cancel 'move via-node' upon mouse up, when not hoovering over network node", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const engine = new PlannerEngineImpl(setup.context);
      const oldPlan = setup.createTwoLegPlan();

      const oldViaNodeFeature = PlannerMapFeature.viaFlag(oldPlan.legs.get(0).sink.featureId);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([oldViaNodeFeature]), [2.1, 2.1]);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.crosshair.expectVisible(false);
      setup.routeLayer.expectFlagCount(3);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, oldPlan.source.featureId, [1, 1]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, oldPlan.sink.featureId, [3, 3]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, oldViaNodeFeature.featureId, [2.1, 2.1]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([3, 3]);
      setup.elasticBand.expectPosition([2.1, 2.1]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List(), [2.5, 2.5]);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeFalsy();

      const newPlan = setup.context.plan;
      expect(newPlan.legs.size).toEqual(2);
      const newLeg1 = newPlan.legs.get(0);
      const newLeg2 = newPlan.legs.get(1);
      expect(newPlan.source.nodeId).toEqual("1001");
      expect(newPlan.sink.nodeId).toEqual("1003");

      expect(newLeg1.source.nodeId).toEqual("1001");
      expect(newLeg1.sink.nodeId).toEqual("1002");

      expect(newLeg2.source.nodeId).toEqual("1002");
      expect(newLeg2.sink.nodeId).toEqual("1003");

      TestSupport.expectCoordinates(newLeg1.coordinates(), [1, 1], [2, 2]);
      TestSupport.expectCoordinates(newLeg2.coordinates(), [2, 2], [3, 3]);

      setup.crosshair.expectVisible(true);
      setup.crosshair.expectPosition([2.5, 2.5]);

      setup.routeLayer.expectFlagCount(3);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, newPlan.source.featureId, [1, 1]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, newPlan.sink.featureId, [3, 3]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, newPlan.legs.get(0).sink.featureId, [2, 2]);

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
      const newViaNodeFeature = PlannerMapFeature.networkNode("1003", "03", [3, 3]);
      setup.createLeg(setup.node1, setup.node3);
      setup.createLeg(setup.node3, setup.node2);
      const oldLegFeature = PlannerMapFeature.leg(oldPlan.legs.get(0).featureId);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([oldLegFeature]), [1.5, 1.5]);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.crosshair.expectVisible(false);
      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, oldPlan.source.featureId, [1, 1]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, oldPlan.sink.featureId, [2, 2]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([2, 2]);
      setup.elasticBand.expectPosition([1.5, 1.5]);

      // act - drag ongoing
      const eventIsNotFurtherPropagated2 = engine.handleDragEvent(List(), [1.7, 1.7]);

      // assert - drag ongoing
      expect(eventIsNotFurtherPropagated2).toBeTruthy();

      setup.crosshair.expectVisible(false);

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, oldPlan.source.featureId, [1, 1]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, oldPlan.sink.featureId, [2, 2]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([2, 2]);
      setup.elasticBand.expectPosition([1.7, 1.7]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List([newViaNodeFeature]), [3.1, 3.1]);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeTruthy();

      const newPlan = setup.context.plan;
      expect(newPlan.legs.size).toEqual(2);
      const newLeg1 = newPlan.legs.get(0);
      const newLeg2 = newPlan.legs.get(1);
      expect(newPlan.source.nodeId).toEqual("1001");
      expect(newPlan.sink.nodeId).toEqual("1002");

      expect(newLeg1.source.nodeId).toEqual("1001");
      expect(newLeg1.sink.nodeId).toEqual("1003");

      expect(newLeg2.source.nodeId).toEqual("1003");
      expect(newLeg2.sink.nodeId).toEqual("1002");

      TestSupport.expectCoordinates(newLeg1.coordinates(), [1, 1], [3, 3]);
      TestSupport.expectCoordinates(newLeg2.coordinates(), [3, 3], [2, 2]);

      setup.crosshair.expectVisible(true);
      setup.crosshair.expectPosition([3.1, 3.1]); // no snap

      setup.routeLayer.expectFlagCount(3);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, newPlan.source.featureId, [1, 1]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, newPlan.sink.featureId, [2, 2]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, newPlan.legs.get(0).sink.featureId, [3, 3]);

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

      const oldLegFeature = PlannerMapFeature.leg(oldPlan.legs.get(0).featureId);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(List([oldLegFeature]), [1.5, 1.5]);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.crosshair.expectVisible(false);
      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, oldPlan.source.featureId, [1, 1]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, oldPlan.sink.featureId, [2, 2]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([2, 2]);
      setup.elasticBand.expectPosition([1.5, 1.5]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleUpEvent(List(), [1.7, 1.7]);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeFalsy();

      const newPlan = setup.context.plan;
      expect(newPlan.legs.size).toEqual(1);
      const newLeg = newPlan.legs.get(0);
      expect(newPlan.source.nodeId).toEqual("1001");
      expect(newPlan.sink.nodeId).toEqual("1002");

      expect(newLeg.source.nodeId).toEqual("1001");
      expect(newLeg.sink.nodeId).toEqual("1002");

      TestSupport.expectCoordinates(newLeg.coordinates(), [1, 1], [2, 2]);

      setup.crosshair.expectVisible(true);
      setup.crosshair.expectPosition([1.7, 1.7]);

      setup.routeLayer.expectFlagCount(2);
      setup.routeLayer.expectFlagExists(PlanFlagType.Start, newPlan.source.featureId, [1, 1]);
      setup.routeLayer.expectFlagExists(PlanFlagType.Via, newPlan.sink.featureId, [2, 2]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.commandStack.commandCount).toEqual(0);

    });

  });

});
