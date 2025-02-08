import { Subset } from '@api/custom/subset';

export class StatisticConfiguration {
  constructor(
    readonly id: string,
    readonly fact: string,
    readonly markdown: boolean,
    readonly linkFunction: (factId: string, subset: Subset) => string | null,
    readonly name: string,
    readonly comment: string
  ) {}
}
