import {List} from "immutable";
import {RouteLeg} from "../../../kpn/shared/planner/route-leg";
import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";

export class DirectionsTestSetup {

  static examplePlan(): Plan {
    const routeLeg = RouteLeg.fromJSON(this.exampleRouteLegJson())
    const planLeg = PlanLeg.fromRouteLeg(routeLeg);
    return new Plan(planLeg.source, List([planLeg]));
  }

  private static exampleRouteLegJson() {
    return {
      "legId": "227",
      "routes": [
        {
          "source": {
            "nodeId": "42749594",
            "nodeName": "32",
            "lat": "51.4822082",
            "lon": "4.4470508"
          },
          "sink": {
            "nodeId": "289197467",
            "nodeName": "08",
            "lat": "51.4689467",
            "lon": "4.4470508"
          },
          "segments": [
            {
              "meters": 2286,
              "surface": "paved",
              "fragments": [
                {
                  "streetIndex": 2,
                  "orientation": 184,
                  "lon": "4.4614065",
                  "meters": 21,
                  "lat": "51.4820128"
                },
                {
                  "streetIndex": 2,
                  "orientation": 189,
                  "lon": "4.4613848",
                  "meters": 9,
                  "lat": "51.4819317"
                },
                {
                  "streetIndex": 0,
                  "orientation": 198,
                  "lon": "4.4612744",
                  "meters": 25,
                  "lat": "51.4817146"
                },
                {
                  "streetIndex": 0,
                  "orientation": 193,
                  "lon": "4.4609198",
                  "meters": 112,
                  "lat": "51.4807315"
                },
                {
                  "streetIndex": 0,
                  "orientation": 190,
                  "lon": "4.4606482",
                  "meters": 104,
                  "lat": "51.4798085"
                },
                {
                  "streetIndex": 0,
                  "orientation": 188,
                  "lon": "4.4604536",
                  "meters": 96,
                  "lat": "51.4789497"
                },
                {
                  "streetIndex": 0,
                  "orientation": 188,
                  "lon": "4.4602667",
                  "meters": 95,
                  "lat": "51.4781000"
                },
                {
                  "streetIndex": 0,
                  "orientation": 185,
                  "lon": "4.4602145",
                  "meters": 38,
                  "lat": "51.4777525"
                },
                {
                  "streetIndex": 0,
                  "orientation": 179,
                  "lon": "4.4603322",
                  "meters": 469,
                  "lat": "51.4735300"
                },
                {
                  "streetIndex": 0,
                  "orientation": 173,
                  "lon": "4.4603548",
                  "meters": 12,
                  "lat": "51.4734227"
                },
                {
                  "streetIndex": 0,
                  "orientation": 165,
                  "lon": "4.4604010",
                  "meters": 12,
                  "lat": "51.4733162"
                },
                {
                  "streetIndex": 0,
                  "orientation": 157,
                  "lon": "4.4604610",
                  "meters": 10,
                  "lat": "51.4732288"
                },
                {
                  "streetIndex": 0,
                  "orientation": 153,
                  "lon": "4.4605351",
                  "meters": 11,
                  "lat": "51.4731388"
                },
                {
                  "streetIndex": 0,
                  "orientation": 149,
                  "lon": "4.4606145",
                  "meters": 10,
                  "lat": "51.4730577"
                },
                {
                  "streetIndex": 0,
                  "orientation": 143,
                  "lon": "4.4606932",
                  "meters": 9,
                  "lat": "51.4729927"
                },
                {
                  "streetIndex": 0,
                  "orientation": 139,
                  "lon": "4.4607957",
                  "meters": 10,
                  "lat": "51.4729190"
                },
                {
                  "streetIndex": 3,
                  "orientation": 212,
                  "lon": "4.4601915",
                  "meters": 78,
                  "lat": "51.4723208"
                },
                {
                  "streetIndex": 3,
                  "orientation": 205,
                  "lon": "4.4601315",
                  "meters": 9,
                  "lat": "51.4722408"
                },
                {
                  "streetIndex": 3,
                  "orientation": 192,
                  "lon": "4.4601015",
                  "meters": 10,
                  "lat": "51.4721508"
                },
                {
                  "streetIndex": 3,
                  "orientation": 176,
                  "lon": "4.4601315",
                  "meters": 33,
                  "lat": "51.4718508"
                },
                {
                  "streetIndex": 3,
                  "orientation": 168,
                  "lon": "4.4605926",
                  "meters": 157,
                  "lat": "51.4704687"
                },
                {
                  "streetIndex": 3,
                  "orientation": 174,
                  "lon": "4.4606051",
                  "meters": 8,
                  "lat": "51.4703939"
                },
                {
                  "streetIndex": 1,
                  "orientation": 254,
                  "lon": "4.4574520",
                  "meters": 227,
                  "lat": "51.4698145"
                },
                {
                  "streetIndex": 1,
                  "orientation": 258,
                  "lon": "4.4570142",
                  "meters": 30,
                  "lat": "51.4697581"
                },
                {
                  "streetIndex": 1,
                  "orientation": 263,
                  "lon": "4.4546011",
                  "meters": 168,
                  "lat": "51.4695711"
                },
                {
                  "streetIndex": 1,
                  "orientation": 262,
                  "lon": "4.4539856",
                  "meters": 43,
                  "lat": "51.4695189"
                },
                {
                  "streetIndex": 1,
                  "orientation": 263,
                  "lon": "4.4521131",
                  "meters": 130,
                  "lat": "51.4693656"
                },
                {
                  "streetIndex": 1,
                  "orientation": 263,
                  "lon": "4.4492756",
                  "meters": 198,
                  "lat": "51.4691360"
                },
                {
                  "lat": "51.4691201",
                  "lon": "4.4490454",
                  "meters": 16,
                  "orientation": 264
                },
                {
                  "lat": "51.4691143",
                  "lon": "4.4489628",
                  "meters": 5,
                  "orientation": 264
                },
                {
                  "lat": "51.4691106",
                  "lon": "4.4489087",
                  "meters": 3,
                  "orientation": 264
                },
                {
                  "lat": "51.4691049",
                  "lon": "4.4488279",
                  "meters": 5,
                  "orientation": 264
                },
                {
                  "lat": "51.4691012",
                  "lon": "4.4487734",
                  "meters": 3,
                  "orientation": 264
                },
                {
                  "lat": "51.4690916",
                  "lon": "4.4486372",
                  "meters": 9,
                  "orientation": 264
                },
                {
                  "lat": "51.4689467",
                  "lon": "4.4470508",
                  "meters": 111,
                  "orientation": 262
                }
              ]
            }
          ],
          "streets": [
            "Essenseweg",
            "Grensstraat",
            "Kerkplein",
            "Kloosterweg"
          ],
          "meters": 2286
        }
      ]
    };
  }

}
