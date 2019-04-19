import {DirectionsAnalyzer} from "./directions-analyzer";
import {DirectionsTestSetup} from "./directions-test-setup";

fdescribe("directions", () => {

  it("translate plan into directions", () => {
    const plan = DirectionsTestSetup.examplePlan();
    // console.log("DEBUG plan\n" + JSON.stringify(plan, null, 2));

    plan.legs.forEach(leg => {
      leg.routes.forEach(route => {
        route.segments.forEach(segment => {
          segment.fragments.forEach(fragment => {
            if (fragment.streetIndex == null) {
              console.log("DEBUG --- " + fragment.meters + " " + fragment.orientation);
            }
            else {
              console.log("DEBUG " + route.streets.get(fragment.streetIndex) + " " +  + fragment.meters + " " + fragment.orientation);
            }
          });
        });
      });
    });
  });

});
