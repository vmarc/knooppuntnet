import { NgClass } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { LinkInfo } from '@api/common/route/link-info';
import { StructureImagesComponent } from '@app/tryout/structure/components/structure-images.component';
import { StructurePageService } from '@app/tryout/structure/structure-page.service';

@Component({
  selector: 'ui-structure-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <table class="kpn-table">
      @for (rowIndex of rowIndexes(); track rowIndex) {
        <tr>
          @for (columnIndex of columnIndexes; track columnIndex) {
            @let linkIndex = rowIndex * columnCount + columnIndex;
            @let linkInfo = linkInfos()[linkIndex];
            <td [ngClass]="{ selected: isSelected(linkInfo) }">
              @if (linkIndex < linkInfos().length) {
                <ui-structure-images [linkInfo]="linkInfo" />
              }
            </td>
          }
        </tr>
      }
    </table>
  `,
  styles: `
    td {
      background-color: #fdfdfd;
    }

    .selected {
      background-color: lightgray;
    }
  `,
  imports: [NgClass, StructureImagesComponent],
})
export class StructureTableComponent {
  private readonly service = inject(StructurePageService);
  protected linkInfos = this.service.linkInfos;
  protected columnCount = this.service.columnCount;
  protected columnIndexes = this.service.columnIndexes;
  protected rowIndexes = this.service.rowIndexes;

  isSelected(linkInfo: LinkInfo): boolean {
    return this.service.isSelected(linkInfo);
  }
}
