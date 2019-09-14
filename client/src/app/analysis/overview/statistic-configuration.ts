import {Subset} from "../../kpn/shared/subset";

export class StatisticConfiguration {

  constructor(readonly id: string,
              readonly fact: string,
              readonly name: string,
              readonly markdown: boolean,
              readonly comment: string,
              readonly linkFunction: (id: string, subset: Subset) => string | null) {
  }

}
