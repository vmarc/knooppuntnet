import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { Params } from '@angular/router';
import { ChangeSetPage } from '@api/common/changes/change-set-page';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/services';
import { Util } from '@app/shared/components/util';
import { RouterService } from '../../shared/services/router.service';

class ChangeSetKey {
  constructor(
    readonly changeSetId: string,
    readonly replicationNumber: string
  ) {}
}

export class ChangeSetPageService {
  private readonly apiService = inject(ApiService);
  private readonly routerService = inject(RouterService);
  readonly key = this.interpretParams(this.routerService.params());
  private _response = signal<ApiResponse<ChangeSetPage>>(null);
  readonly response = this._response.asReadonly();
  readonly changeSetTitle = this.initChangeSetTitle();

  onInit(): void {
    const key = this.interpretParams(this.routerService.params());
    this.apiService
      .changeSet(key.changeSetId, key.replicationNumber)
      .subscribe((response) => this._response.set(response));
  }

  private interpretParams(params: Params): ChangeSetKey {
    const changeSetId = params['changeSetId'];
    const replicationNumber = params['replicationNumber'];
    return new ChangeSetKey(changeSetId, replicationNumber ?? 0);
  }

  private initChangeSetTitle(): string {
    if (+this.key.replicationNumber > 0) {
      return this.key.changeSetId + ' ' + Util.replicationName(+this.key.replicationNumber);
    }
    return this.key.changeSetId;
  }
}
