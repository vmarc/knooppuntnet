export class NetworkRouteFilterCriteria {

  constructor(readonly investigate: boolean = null,
              readonly accessible: boolean = null,
              readonly roleConnection: boolean = null,
              readonly relationLastUpdated: string = null /*TimeFilterKind.Value = TimeFilterKind.ALL*/) {
  }
}
