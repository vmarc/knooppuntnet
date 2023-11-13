export class FilterOption {
  constructor(
    readonly name: string,
    readonly count: number,
    readonly selected: boolean,
    readonly updateState: () => void
  ) {}
}
