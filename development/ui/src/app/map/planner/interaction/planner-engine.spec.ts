import {List} from "immutable";
import {of} from "rxjs";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {RouteLeg} from "../../../kpn/shared/planner/route-leg";
import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlannerMapFeature} from "../features/planner-map-feature";
import {PlannerMapFeatureLegNode} from "../features/planner-map-feature-leg-node";
import {PlannerMapFeatureNetworkNode} from "../features/planner-map-feature-network-node";
import {Plan} from "../plan/plan";
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
      const appService = jasmine.createSpyObj("appService", ["routeLeg"]);
      const engine = new PlannerEngineImpl(setup.context, appService);
      engine.handleMouseOut();
      setup.crosshair.expectVisible(false);
    });

    it("should make crosshair visible again upon mouse enter", () => {
      const setup = new PlannerTestSetup();
      const appService = jasmine.createSpyObj("appService", ["routeLeg"]);
      const engine = new PlannerEngineImpl(setup.context, appService);
      engine.handleMouseEnter();
      setup.crosshair.expectVisible(true);
    });

  });

  describe("add start node", () => {

    it("should add start node upon handle down event while hoovering over network node", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const appService = jasmine.createSpyObj("appService", ["routeLeg"]);
      const engine = new PlannerEngineImpl(setup.context, appService);

      const node = new PlannerMapFeatureNetworkNode("1001", "01", [1, 1,]);

      const features: List<PlannerMapFeature> = List([node]);

      // act
      const eventIsNotFurtherPropagated = engine.handleDownEvent(features, [1.1, 1.1]);

      // assert
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.crosshair.expectVisible(true);
      setup.crosshair.expectPosition([1, 1]); // snapped to network node coordinate, not cursor position

      setup.routeLayer.expectStartNodeCount(1);
      setup.routeLayer.expectStartNodeExists("1001", [1, 1]);

      expect(setup.context.plan.source.nodeId).toEqual("1001");
      expect(setup.context.plan.legs.size).toEqual(0);

      expect(setup.context.commandStack.commandCount).toEqual(1);
    });

  });

  describe("add leg when plan already has start point", () => {

    it("should add new leg upon handle down event while hoovering over network node", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const appService = jasmine.createSpyObj("appService", ["routeLeg"]);
      const engine = new PlannerEngineImpl(setup.context, appService);

      const node1 = new PlannerMapFeatureNetworkNode("1001", "01", [1, 1]);
      const node2 = new PlannerMapFeatureNetworkNode("1002", "02", [2, 2]);

      setup.context.updatePlan(new Plan(node1, List()));

      const features: List<PlannerMapFeature> = List([node2]);

      const routeLeg = new RouteLeg("12", List());
      const response = new ApiResponse<RouteLeg>(null, null, routeLeg);

      appService.routeLeg.and.returnValue(of(response));

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

      setup.routeLayer.expectViaNodeCount(1);
      setup.routeLayer.expectViaNodeExists(setup.context.plan.legs.get(0).legId, "1002", [2, 2]);

      expect(setup.context.commandStack.commandCount).toEqual(1);
    });

  });

  describe("move start-node", () => {

    it("should handle start node drag", () => {

      // arrange
      const setup = new PlannerTestSetup();
      const appService = jasmine.createSpyObj("appService", ["routeLeg"]);
      const engine = new PlannerEngineImpl(setup.context, appService);

      const oldStartNodeFeature = new PlannerMapFeatureLegNode("start-node-flag-1001", "1001");
      const newStartNodeFeature = new PlannerMapFeatureNetworkNode("1002", "02", [2, 2]);

      const oldStartNode = new PlanNode("1001", "01", [1, 1,]);
      setup.context.updatePlan(new Plan(oldStartNode, List()));

      const features: List<PlannerMapFeature> = List([oldStartNodeFeature]);

      // act - start drag
      const eventIsNotFurtherPropagated = engine.handleDownEvent(features, [1.1, 1.1]);

      // assert - drag started
      expect(eventIsNotFurtherPropagated).toBeTruthy();

      setup.crosshair.expectVisible(false);

      setup.routeLayer.expectStartNodeExists("start-node-flag-1001", [1.1, 1.1]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.1, 1.1]);

      // act - drag ongoing
      const eventIsNotFurtherPropagated2 = engine.handleDragEvent(List(), [1.5, 1.5]);

      // assert - drag ongoing
      expect(eventIsNotFurtherPropagated2).toBeTruthy();

      setup.crosshair.expectVisible(false);

      setup.routeLayer.expectStartNodeExists("start-node-flag-1001", [1.5, 1.5]);

      setup.elasticBand.expectVisible(true);
      setup.elasticBand.expectAnchor1([1, 1]);
      setup.elasticBand.expectAnchor2([1, 1]);
      setup.elasticBand.expectPosition([1.5, 1.5]);

      // act - drag end
      const eventIsNotFurtherPropagated3 = engine.handleDragEvent(List([newStartNodeFeature]), [2.1, 2.1]);

      // assert - drag end
      expect(eventIsNotFurtherPropagated3).toBeTruthy();

      setup.crosshair.expectVisible(true);
      setup.crosshair.expectPosition([2, 2]);

      setup.routeLayer.expectStartNodeCount(1);
      setup.routeLayer.expectStartNodeExists("start-node-flag-1002", [2, 2]);

      setup.elasticBand.expectVisible(false);

      expect(setup.context.plan.source.nodeId).toEqual("1002");
      expect(setup.context.plan.legs.size).toEqual(0);

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
