import { Injectable } from '@angular/core';
import { PageState } from './page-state';

@Injectable({
  providedIn: 'root',
})
export class StateService {
  readonly page = new PageState();
}
