import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { UserService } from './user.service';
import { UserErrorComponent } from './user-error.component';

@Component({
  selector: 'kpn-user-login',
  standalone: true,
  template: `
    <h1>Login</h1>
    <p>Here goes some explanation about the login process.</p>
    <p>Click the login button to login via the OpenStreetMap website.</p>
    <p>Click the cancel link to return to the previous page.</p>
    <div class="menu top-spacer">
      <a href="/oauth2/authorization/osm">REAL LINK</a>
      <button (click)="login()">login</button>
      <a (click)="cancel()">cancel</a>
    </div>
    <kpn-user-error />
  `,
  imports: [UserErrorComponent],
})
export class UserLoginComponent {
  private readonly userService = inject(UserService);

  login(): void {
    //this.userService.login();
  }

  cancel(): void {
    this.userService.navigateToReturnUrl();
  }
}
