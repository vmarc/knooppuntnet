import {PlannerTestSetup} from "../context/planner-test-setup";
import {PlannerEngineImpl} from "./planner-engine-impl";

describe("PlannerEngine", () => {

  it("xxx", () => {

    const setup = new PlannerTestSetup();
    const appService = jasmine.createSpyObj("appService", ["routeLeg"]);
    const engine = new PlannerEngineImpl(setup.context, appService);


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

    it("xxx", () => {

    });

  });

  describe("add leg", () => {

    it("xxx", () => {

    });

  });

  describe("move start-node", () => {

    it("xxx", () => {

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
