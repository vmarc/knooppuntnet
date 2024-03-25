import { DOCUMENT } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import * as Sentry from '@sentry/angular-ivy';

@Injectable()
export class UserService {
  private readonly http = inject(HttpClient);
  private readonly document = inject(DOCUMENT);

  private readonly _user = signal<string>(null);
  readonly user = this._user.asReadonly();
  readonly loggedIn = computed(() => this.user() !== null);

  constructor() {
    this.http.get('/api/oauth2/user', { responseType: 'text' }).subscribe((user) => {
      if (user && user.length > 0) {
        this.updateUser(user);
      }
    });
  }

  login(): void {
    this.document.location.assign('/api/oauth2/authorization/osm');
  }

  logout(): void {
    this.http.post('/api/oauth2/logout', { responseType: 'text' }).subscribe({
      next: () => this.updateUser(null),
      error: () => this.updateUser(null),
    });
  }

  private updateUser(user: string | null): void {
    if (user) {
      Sentry.setUser({
        _id: user,
      });
    } else {
      Sentry.setUser(null);
    }
    this._user.set(user);
  }
}
