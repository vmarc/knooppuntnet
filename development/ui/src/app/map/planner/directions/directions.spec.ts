import {DirectionsAnalyzer} from "./directions-analyzer";
import {DirectionsTestSetup} from "./directions-test-setup";

describe("directions", () => {

  it("translate plan into directions", () => {
    const plan = DirectionsTestSetup.examplePlan();
    // console.log("DEBUG plan\n" + JSON.stringify(plan, null, 2));

    const instructions = new DirectionsAnalyzer().analyze(plan);
    instructions.forEach(instruction => {
      if (instruction.node != null) {
        console.log("node " + instruction.node);
      } else {
        let text = "";
        // if (instruction.text != null) {
        //   text = text + instruction.text + " ";
        // }
        if (instruction.command != null) {
          text = text + instruction.command + " ";
        }
        if (instruction.street != null) {
          text = text + instruction.street + " ";
        }
        text = text + instruction.distance + " m";
        console.log(text);
      }
    });
  });

});
