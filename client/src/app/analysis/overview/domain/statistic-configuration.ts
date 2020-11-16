import {Subset} from '../../../kpn/api/custom/subset';

export class StatisticConfiguration {

  constructor(readonly id: string,
              readonly fact: string,
              readonly name: string,
              readonly markdown: boolean,
              readonly comment: string,
              readonly linkFunction: (id: string, subset: Subset) => string | null) {
  }

}
