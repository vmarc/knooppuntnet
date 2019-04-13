import {List} from "immutable";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlannerMapFeature} from "../features/planner-map-feature";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanNode} from "../plan/plan-node";
import {PlannerEngineImpl} from "./planner-engine-impl";

describe("PlannerEngine", () => {

  it("xxx", () => {

  });

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

      const node = PlannerMapFeature.networkNode("1001", "01", [1, 1,]);
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

      setup.context.updatePlan(new Plan(node1.node, List()));

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

      const oldStartNode = PlanNode.create("1001", "01", [1, 1,]);
      setup.context.updatePlan(new Plan(oldStartNode, List()));

      const oldStartNodeFeature = PlannerMapFeature.startFlag(oldStartNode.featureId);
      setup.routeLayer.addFlag(PlanFlag.fromStartNode(oldStartNode));
      const newStartNodeFeature = PlannerMapFeature.networkNode("1002", "02", [2, 2]);

      const features: List<PlannerMapFeature> = List([oldStartNodeFeature]);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(features, [1.1, 1.1]);

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
      // TODO assert command?
    });

    it("should cancel 'move start-node' upon mouse up, while not hoovering over network node", () => {

    });

  });

  describe("move end-node", () => {

    it("xxx", () => {

    });

    it("should cancel 'move end-node' upon mouse up, while not hoovering over network node", () => {

    });

  });

  describe("move via-node", () => {

    it("xxx", () => {

    });

    it("should cancel 'move via-node' upon mouse up, while not hoovering over network node", () => {

    });

  });

  describe("split leg", () => {

    it("xxx", () => {

    });

    it("should cancel 'split leg' upon mouse up, while not hoovering over network node", () => {

    });

  });

});
