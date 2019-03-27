import {PlanAbstractMember} from "./plan-abstract-member";

export class PlanLeg extends PlanAbstractMember {

  constructor(public id: string,
              public distance: number,
              public coordinates: Array<Coordinates>) {
    super();
  }

  isLeg(): boolean {
    return true;
  }

  toLeg(): PlanLeg {
    return this;
  }

}
