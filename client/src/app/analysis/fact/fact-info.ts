import {Fact} from "../../kpn/api/custom/fact";
import {Ref} from "../../kpn/api/common/common/ref";

export class FactInfo {
  constructor(public fact: Fact,
              public networkRef?: Ref,
              public routeRef?: Ref,
              public nodeRef?: Ref) {
  }
}
