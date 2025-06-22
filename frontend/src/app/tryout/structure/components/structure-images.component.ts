import { computed } from '@angular/core';
import { input } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { LinkInfo } from '@api/common/route/link-info';
import { StructureCanvasWrapperComponent } from '@app/shared/components/structure/structure-canvas-wrapper.component';
import { StructurePageService } from '@app/tryout/structure/structure-page.service';

@Component({
  selector: 'ui-structure-images',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="link-box" (click)="selectLink()">
      <img [src]="src()" [alt]="linkInfo().name" class="box" />
      <div class="box">
        <ui-structure-canvas-wrapper memberType="way" [link]="link()" />
      </div>
    </div>
  `,
  styles: `
    .link-box {
      display: flex;
      gap: 2px;
    }

    .box {
      border: 1px solid lightgray;
      background-color: white;
    }
  `,
  imports: [StructureCanvasWrapperComponent],
})
export class StructureImagesComponent {
  private readonly service = inject(StructurePageService);

  readonly linkInfo = input.required<LinkInfo>();
  protected readonly src = computed(() => '/assets/images/links/' + this.linkInfo().name + '.png');
  protected readonly link = computed(() => this.linkInfo().link);

  selectLink(): void {
    this.service.selectLink(this.linkInfo());
  }
}
