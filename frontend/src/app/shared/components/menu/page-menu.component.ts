import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MenuOption } from '@app/shared/components/menu/menu-option';
import { PageMenuOptionComponent } from '@app/shared/components/menu/page-menu-option.component';

@Component({
  selector: 'ui-page-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="menu-wrapper">
      <div class="menu">
        @if (options()) {
          @for (option of options(); track option.pageName) {
            <ui-page-menu-option
              [link]="option.pageLink"
              [active]="pageName() === option.pageName"
              [elementCount]="option.elementCount"
              >{{ option.label }}
            </ui-page-menu-option>
          }
        }
        <ng-content />
      </div>
      <div class="menu-extra">
        <ng-content select="[menu-extra-item]" />
      </div>
    </div>
  `,
  styles: `
    .menu-wrapper {
      display: flex;
      border-bottom: 1px solid lightgray;
    }

    .menu {
      line-height: 30px;
    }

    .menu-extra {
      line-height: 30px;
      margin-left: auto;
    }

    ::ng-deep .menu :not(:last-child):after {
      content: ' | ';
      padding-left: 5px;
      padding-right: 5px;
    }
  `,
  imports: [PageMenuOptionComponent],
})
export class PageMenuComponent {
  readonly pageName = input<string>();
  readonly options = input<MenuOption[]>();
}
