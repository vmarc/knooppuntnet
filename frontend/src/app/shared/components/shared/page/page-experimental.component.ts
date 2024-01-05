import { NgClass } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { VersionService } from '@app/services';

@Component({
  selector: 'kpn-page-experimental',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div [ngClass]="{ hidden: !isExperimental() }" class="warning-message">
      <span i18n="@@experimental.title">Warning: experimental !</span>
      @if (!moreDetailOpen) {
        <span>
          <a id="more" (click)="more()" i18n="@@experimental.more">more</a>
        </span>
      } @else {
        <div>
          <p i18n="@@experimental.message-1">
            This is an experimental version of the knooppuntnet website.
          </p>
          <p i18n="@@experimental.message-2">
            The data may not be completely correct, and some functions may not work as expected.
            Unless you are here for trying out new functionality, you may be better off using the
            <a [href]="link()">stable version</a> of knooppuntnet instead.
          </p>
          <a (click)="less()" i18n="@@experimental.less">less</a>
        </div>
      }
    </div>
  `,
  styles: `
    .warning-message {
      background-color: #ffb6c157;
      padding: 0.8em 3em 0.8em 0.8em;
    }

    #more {
      padding-left: 1em;
    }
  `,
  standalone: true,
  imports: [NgClass],
})
export class PageExperimentalComponent {
  private readonly versionService = inject(VersionService);

  protected moreDetailOpen = false;

  isExperimental(): boolean {
    return this.versionService.experimental;
  }

  more(): void {
    this.moreDetailOpen = true;
  }

  less(): void {
    this.moreDetailOpen = false;
  }

  link(): string {
    return window.location.href.replace('experimental.', '');
  }
}
