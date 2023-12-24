import { OnDestroy } from '@angular/core';
import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { UserStore } from './user.store';

@Component({
  selector: 'kpn-user-error',
  standalone: true,
  template: `
    @if (error(); as message) {
      <p class="kpn-warning">
        {{ message }}
      </p>
    }
    @if (errorDetail(); as message) {
      <p class="kpn-warning">
        {{ message }}
      </p>
    }
  `,
})
export class UserErrorComponent implements OnDestroy {
  private readonly userStore = inject(UserStore);
  readonly error = this.userStore.error;
  readonly errorDetail = this.userStore.errorDetail;

  ngOnDestroy() {
    this.userStore.resetError();
  }
}
