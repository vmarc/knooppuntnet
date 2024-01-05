import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { EditService } from '@app/components/shared';
import { DeviceDetectorService } from 'ngx-device-detector';

@Component({
  selector: 'kpn-josm',
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->

    <h1>JOSM</h1>
    <ul>
      <li class="level1">
        <span>Test existing situation</span>
        <ul>
          <li>Represents how the josm link is currently implemented in knooppuntnet.</li>
          <li>This is expected not to work in Safari.</li>
          <li>Link: <a rel="nofollow" (click)="josm1()">josm</a>.</li>
        </ul>
      </li>

      <li class="level1">
        <span>Test browser tab</span>
        <ul>
          <li>Test to see if opening in separate browser tab works on Safari.</li>
          <li>
            Still have to work out if we can show a user friendly message in case JOSM is not
            started, or remote control not switched on. Currently a standard browser error message
            will appear in the new browser tab that is opened. Something more user friendly would be
            nice, but not sure this is technically possible.
          </li>
          <li>Still have to work out how to do multiple sequential requests.</li>
          <li>
            Link:
            <a [href]="url" target="josm">josm</a>.
          </li>
        </ul>
      </li>

      <li class="level1">
        <span>Test browser tab 2</span>
        <ul>
          <li>Link: <a rel="nofollow" (click)="josm4()">josm</a>.</li>
        </ul>
      </li>

      <li class="level1">
        <span>Test iframe</span>
        <ul>
          <li>Test to see if talking to josm through iframe works on Safari.</li>
          <li>
            Still have to work out if we can show a user friendly message in case JOSM is not
            started, or remote control not switched on. Currently no feedback at all.
          </li>
          <li>Still have to work out how to do multiple sequential requests.</li>
          <li>
            Still need mechanism to keep re-using the same iframe instead of creating a new one each
            time.
          </li>
          <li>Link: <a rel="nofollow" (click)="josm3()">josm</a>.</li>
        </ul>
      </li>

      <li class="level1">
        <span>Test browser</span>
        <ul>
          <li>
            If we end up needing to do different logic for Safari, then we need to be able to
            determine the current browser type.
          </li>
          <li>Hopefully this will not be needed.</li>
          <li>
            Test result: the application thinks the current browser is
            <i>{{ browser }}</i
            >.
          </li>
          <li>
            Second mechanism: this browser is
            @if (isSafari()) {
              Safari
            } @else {
              NOT Safari
            }
          </li>
        </ul>
      </li>
    </ul>
  `,
  styles: `
    .level1 {
      padding-bottom: 1em;
    }
  `,
  standalone: true,
})
export class JosmComponent {
  private readonly deviceService = inject(DeviceDetectorService);
  private readonly editService = inject(EditService);
  protected readonly browser = this.deviceService.browser;
  private url1 = 'http://127.0.0.1:8111/import?url=https://api.openstreetmap.org/api/0.6/relation/';
  private url2 = '/full';
  url = `${this.url1}272946${this.url2}`;

  josm1(): void {
    this.editService.edit({
      relationIds: [9624148],
      fullRelation: true,
    });
  }

  josm3(): void {
    const iframe = document.createElement('iframe');
    iframe.style.display = 'none';
    iframe.onload = (e) => {
      console.log(['iframe onload', e]);
    };
    iframe.setAttribute('src', this.url);
    document.body.appendChild(iframe);
  }

  josm4() {
    const u1 = `${this.url1}272946${this.url2}`;
    const u2 = `${this.url1}272947${this.url2}`;
    const u3 = `${this.url1}1687264${this.url2}`;

    setTimeout(() => window.open(u1, 'josm'), 1000);
    setTimeout(() => window.open(u2, 'josm'), 2000);
    setTimeout(() => window.open(u3, 'josm'), 3000);
  }

  isSafari(): boolean {
    return !!window['safari'];
  }
}
