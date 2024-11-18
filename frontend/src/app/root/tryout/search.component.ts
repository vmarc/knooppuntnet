import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MatInput } from '@angular/material/input';
import { MatNavList } from '@angular/material/list';
import { MatListItem } from '@angular/material/list';
import { MatFormField } from '@angular/material/select';
import { MatLabel } from '@angular/material/select';
import { GeocoderLocation } from '@api/common/geocoder-location';
import { DividerComponent } from '@app/components/shared';
import { BackButtonComponent } from '@app/components/shared';
import { PageButtonsComponent } from '@app/components/shared/page';
import { ApiService } from '@app/services';
import { PageComponent } from '../../shared/components/shared/page/page.component';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'kpn-search',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-buttons>
      <kpn-back-button />
      <mat-label>Search</mat-label>
    </kpn-page-buttons>
    <kpn-page>
      <form [formGroup]="form" class="kpn-form" #ngForm="ngForm">
        <mat-form-field appearance="outline">
          <mat-label>Search</mat-label>
          <input matInput placeholder="location, relation id" [formControl]="query" />
        </mat-form-field>
        <div>
          <button mat-stroked-button (click)="search()">Search</button>
        </div>
      </form>
      @if (geocoderLocations().length > 0) {
        <kpn-divider />
        <p>Locations</p>
        <mat-nav-list>
          @for (location of geocoderLocations(); track location.name) {
            <mat-list-item>
              {{ location.name }}
            </mat-list-item>
          }
        </mat-nav-list>
        <kpn-divider />
      }
    </kpn-page>
  `,
  standalone: true,
  imports: [
    BackButtonComponent,
    MatLabel,
    PageButtonsComponent,
    MatFormField,
    MatInput,
    PageComponent,
    MatButton,
    FormsModule,
    ReactiveFormsModule,
    DividerComponent,
    MatListItem,
    MatNavList,
  ],
})
export class SearchComponent {
  private readonly apiService = inject(ApiService);
  readonly geocoderLocations = signal<GeocoderLocation[]>([]);

  readonly query = new FormControl<string>('');

  readonly form = new FormGroup({
    query: this.query,
  });

  search(): void {
    this.apiService.search(this.query.value).subscribe((response) => {
      this.geocoderLocations.set(response.result.geocoderLocations);
    });
  }
}
