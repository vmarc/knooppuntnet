import { OnDestroy } from '@angular/core';
import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { UserService } from '../service/user.service';
import { UserStore } from '../service/user.store';

@Component({
  selector: 'kpn-login',
  standalone: true,
  template: `
    <h1>Login</h1>
    <p>Here goes some explanation about the login process.</p>
    <p>Click the login button to login via the OpenStreetMap website.</p>
    <p>Click the cancel link to return to the previous page.</p>
    <div class="menu top-spacer">
      <button (click)="login()">login</button>
      <a (click)="cancel()">cancel</a>
    </div>
    @if (error(); as message) {
      <p class="error">
        {{ message }}
      </p>
    }
    @if (errorDetail(); as message) {
      <p class="error">
        {{ message }}
      </p>
    }
  `,
})
export class LoginComponent implements OnDestroy {
  private readonly userService = inject(UserService);
  private readonly userStore = inject(UserStore);
  readonly error = this.userStore.error;
  readonly errorDetail = this.userStore.errorDetail;

  login(): void {
    this.userService.login();
  }

  cancel(): void {
    this.userService.navigateToReturnUrl();
  }

  ngOnDestroy() {
    this.userStore.updateError(null);
    this.userStore.updateErrorDetail(null);
  }
}
