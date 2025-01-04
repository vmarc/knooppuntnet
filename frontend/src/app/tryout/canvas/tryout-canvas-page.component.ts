import { NgClass } from '@angular/common';
import { OnInit } from '@angular/core';
import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { LinkInfo } from '@api/common/route/link-info';
import { ApiService } from '@app/services';
import { TryoutWrapperComponent } from './tryout-wrapper.component';

@Component({
  selector: 'kpn-tryout-canvas-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="page">
      <p>Links: {{ linkCount() }}</p>
      <pre>{{ selectedLinkDescription() }}</pre>
      <div style="display: flex;">
        <div class="selected-link">
          <kpn-tryout-wrapper [linkInfo]="selectedLink()" />
        </div>
        <div style="height:40px;">&nbsp;</div>
      </div>
      <div style="display: flex;">
        <div class="selected-link">
          <kpn-tryout-wrapper [linkInfo]="selectedLink()" />
        </div>
        <div style="height:80px;">&nbsp;</div>
      </div>
      <table class="kpn-table">
        @for (rowIndex of rowIndexes(); track rowIndex) {
          <tr>
            @for (columnIndex of columnIndexes; track columnIndex) {
              @let linkIndex = rowIndex * columnCount + columnIndex;
              @let linkInfo = linkInfos()[linkIndex];
              <td [ngClass]="{ selected: isSelected(linkInfo) }">
                @if (linkIndex < linkInfos().length) {
                  <div class="link-box" (click)="selectLink(linkInfo)">
                    <img
                      [src]="'/assets/images/links/' + linkInfo.name + '.png'"
                      [alt]="linkInfo.name"
                      class="box"
                    />
                    <div class="box">
                      <kpn-tryout-wrapper [linkInfo]="linkInfo" />
                    </div>
                  </div>
                }
              </td>
            }
          </tr>
        }
      </table>
    </div>
  `,
  styles: `
    .page {
      margin: 1em;
    }

    .selected-link {
      display: flex;
      border: 1px solid lightgray;
      margin-bottom: 1em;
    }

    .link-box {
      display: flex;
      gap: 2px;
    }

    td {
      background-color: #fdfdfd;
    }

    .box {
      border: 1px solid lightgray;
      background-color: white;
    }

    .selected {
      background-color: lightgray;
    }
  `,
  imports: [TryoutWrapperComponent, NgClass],
})
export class TryoutCanvasPageComponent implements OnInit {
  readonly columnCount = 10;

  private readonly apiService = inject(ApiService);
  readonly linkInfos = signal<LinkInfo[]>([]);
  readonly linkCount = computed(() => this.linkInfos().length);
  readonly rowCount = computed(() => Math.trunc(this.linkCount() / this.columnCount) + 1);

  readonly columnIndexes = Array.from(Array(this.columnCount).keys());
  readonly rowIndexes = computed(() => {
    return Array.from(Array(this.rowCount()).keys());
  });

  readonly selectedLink = signal<LinkInfo | undefined>(undefined);
  readonly selectedLinkDescription = computed(() => this.selectedLink()?.description);

  ngOnInit(): void {
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
