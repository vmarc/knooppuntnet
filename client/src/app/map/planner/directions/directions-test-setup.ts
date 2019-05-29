import {List} from "immutable";
import {RouteLeg} from "../../../kpn/shared/planner/route-leg";
import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlanLegBuilder} from "../plan/plan-leg-builder";
import {Directions1TestSetup} from "./directions-1-32-08-test-setup";
import {Directions2TestSetup} from "./directions-2-08-93-test-setup";
import {Directions3TestSetup} from "./directions-3-93-92-test-setup";
import {Directions4TestSetup} from "./directions-4-92-11-test-setup";
import {Directions5TestSetup} from "./directions-5-11-91-test-setup";
import {Directions6TestSetup} from "./directions-6-91-34-test-setup";
import {Directions7TestSetup} from "./directions-7-34-35-test-setup";

export class DirectionsTestSetup {

  static examplePlan(): Plan {
    const leg1 = this.toLeg(Directions1TestSetup.json_32_08());
    const leg2 = this.toLeg(Directions2TestSetup.json_08_93());
    const leg3 = this.toLeg(Directions3TestSetup.json_93_92());
    const leg4 = this.toLeg(Directions4TestSetup.json_92_11());
    const leg5 = this.toLeg(Directions5TestSetup.json_11_91());
    const leg6 = this.toLeg(Directions6TestSetup.json_91_34());
    const leg7 = this.toLeg(Directions7TestSetup.json_34_35());
    return Plan.create(leg1.source, List([leg1, leg2, leg3, leg4, leg5, leg6, leg7]));
  }

  private static toLeg(json): PlanLeg {
    const routeLeg = RouteLeg.fromJSON(json);
    return PlanLegBuilder.toPlanLeg2(routeLeg);
  }

}
