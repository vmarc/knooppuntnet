import { computed } from '@angular/core';
import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { StructureImageSelectedComponent } from './components/structure-image-selected.component';
import { StructureTableComponent } from './components/structure-table.component';
import { StructurePageService } from './structure-page.service';

@Component({
  selector: 'ui-structure-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="page">
      @if (linkInfos()) {
        <p>Links: {{ linkCount() }}</p>
        <ui-structure-image-selected />
        <ui-structure-table />
      }
    </div>
  `,
  styles: `
    .page {
      margin: 1em;
    }
  `,
  imports: [StructureImageSelectedComponent, StructureTableComponent],
})
export class StructurePageComponent implements OnInit {
  private readonly service = inject(StructurePageService);
  protected linkInfos = this.service.linkInfos;
  protected linkCount = computed(() => this.linkInfos().length);

  ngOnInit(): void {
    this.service.init();
  }
}
