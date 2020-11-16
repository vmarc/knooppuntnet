import {Component} from '@angular/core';
import {ChangeDetectionStrategy} from '@angular/core';

@Component({
  selector: 'kpn-demo-disabled',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="demo.disabled.text-1">
      Sorry.
    </p>
    <p i18n="demo.disabled.text-2">
      Your window is currently not large enough to show the videos in good quality.
    </p>
    <p i18n="demo.disabled.text-3">
      Please increase your browser window size, or use a device with a larger display to view the videos.
    </p>
  `
})
export class DemoDisabledComponent {
}
