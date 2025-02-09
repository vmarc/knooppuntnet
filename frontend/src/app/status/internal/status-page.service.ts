import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { Status } from '@api/common/status/status';
import { ApiResponse } from '@api/custom/api-response';
import { ApiService } from '@app/shared/services/api.service';
import { StatusLinks } from './status-links';

@Injectable()
export class StatusPageService {
  private readonly apiService = inject(ApiService);

  private readonly _response = signal<ApiResponse<Status>>(null);
  readonly response = this._response.asReadonly();
  private readonly timestamp = computed(() => this.response().result.timestamp);
  readonly replicationLinks = computed(() => this.links('/status/replication'));
  readonly systemLinks = computed(() => this.links('/status/system'));
  readonly logLinks = computed(() => this.links('/status/log'));

  onInit(): void {
    this.apiService.status().subscribe((response) => {
      this._response.set(response);
    });
  }

  private links(root: string): StatusLinks {
    return new StatusLinks(this.timestamp(), root);
  }
}
