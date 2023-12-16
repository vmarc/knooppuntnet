import { HttpClient } from '@angular/common/http';
import { OnDestroy } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs';

@Injectable()
export class Page1Service implements OnDestroy {
  private readonly http = inject(HttpClient);
  private readonly _page = signal<string | null>(null);
  readonly page = this._page.asReadonly();

  constructor() {
    console.log('Page1Service.constructor()');
    this.http
      .get('/api/page1', { responseType: 'text' })
      .pipe(
        tap((response) => {
          console.log('Page1Service.constructor() response=' + response);
          this._page.set(response);
        })
      )
      .subscribe();
  }

  ngOnDestroy(): void {
    console.log('Page1Service.ngOnDestroy()');
  }
}
