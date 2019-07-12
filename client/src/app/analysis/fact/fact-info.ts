import {Fact} from "../../kpn/shared/fact";
import {Ref} from "../../kpn/shared/common/ref";

export class FactInfo {
  constructor(public fact: Fact,
              public networkRef?: Ref,
              public routeRef?: Ref,
              public nodeRef?: Ref) {
  }
}
