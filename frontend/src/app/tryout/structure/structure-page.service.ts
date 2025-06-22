import { Injectable } from '@angular/core';
import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { LinkInfo } from '@api/common/route/link-info';
import { ApiService } from '@app/shared/services/api.service';

@Injectable({
  providedIn: 'root',
})
export class StructurePageService {
  readonly columnCount = 10;

  private readonly apiService = inject(ApiService);
  readonly linkInfos = signal<LinkInfo[]>([]);
  readonly rowCount = computed(() => Math.trunc(this.linkInfos().length / this.columnCount) + 1);

  readonly columnIndexes = Array.from(Array(this.columnCount).keys());
  readonly rowIndexes = computed(() => {
    return Array.from(Array(this.rowCount()).keys());
  });

  readonly selectedLink = signal<LinkInfo | undefined>(undefined);

  init(): void {
    this.apiService.links().subscribe((response) => {
      this.linkInfos.set(response.result);
      this.selectLink(response.result[0]);
    });
  }

  selectLink(linkInfo: LinkInfo): void {
    this.selectedLink.set(linkInfo);
  }

  isSelected(linkInfo: LinkInfo): boolean {
    return this.selectedLink() === linkInfo;
  }
}
