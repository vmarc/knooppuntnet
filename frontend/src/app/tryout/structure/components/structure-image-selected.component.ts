import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { StructureCanvasWrapperComponent } from '@app/shared/components/structure/structure-canvas-wrapper.component';
import { StructurePageService } from '@app/tryout/structure/structure-page.service';

@Component({
  selector: 'ui-structure-image-selected',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (selectedLink(); as link) {
      <pre>{{ selectedLinkDescription() }}</pre>
      <div class="link-box">
        <div class="selected-link">
          <ui-structure-canvas-wrapper memberType="way" [link]="link.link" />
        </div>
        <div style="height:40px;">&nbsp;</div>
      </div>
      <div class="link-box">
        <div class="selected-link">
          <ui-structure-canvas-wrapper memberType="way" [link]="link.link" />
        </div>
        <div style="height:80px;">&nbsp;</div>
      </div>
    }
  `,
  styles: `
    .selected-link {
      display: flex;
      border: 1px solid lightgray;
      margin-bottom: 1em;
    }

    .link-box {
      display: flex;
      gap: 2px;
    }
  `,
  imports: [StructureCanvasWrapperComponent],
})
export class StructureImageSelectedComponent {
  private readonly service = inject(StructurePageService);
  protected selectedLink = this.service.selectedLink;
  protected selectedLinkDescription = computed(() => this.selectedLink()?.description);
}
