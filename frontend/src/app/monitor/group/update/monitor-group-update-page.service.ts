import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Validators } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';
import { initialState } from './monitor-group-update-page.state';
import { MonitorGroupUpdatePageState } from './monitor-group-update-page.state';

@Injectable()
export class MonitorGroupUpdatePageService {
  private readonly navService = inject(NavService);
  private readonly monitorService = inject(MonitorService);

  private readonly _state = signal<MonitorGroupUpdatePageState>(initialState);
  readonly state = this._state.asReadonly();

  private initialName = '';
  readonly name = new FormControl<string>('', {
    validators: [Validators.required, Validators.maxLength(15)],
    asyncValidators: this.monitorService.asyncGroupNameUniqueValidator(
      () => this.initialName
    ),
  });
  readonly description = new FormControl<string>('', [
    Validators.required,
    Validators.maxLength(100),
  ]);

  readonly form = new FormGroup({
    name: this.name,
    description: this.description,
  });

  constructor() {
    const groupName = this.navService.param('groupName');
    const description = this.navService.state('description');
    this._state.update((state) => ({
      ...state,
      groupName,
      groupDescription: description,
    }));

    this.monitorService.group(groupName).subscribe((response) => {
      const groupDescription = response.result?.groupDescription ?? description;
      this._state.update((state) => ({
        ...state,
        groupDescription,
        response,
      }));
      if (response.result) {
        const page = response.result;
        if (page) {
          this.initialName = page.groupName;
          this.form.reset({
            name: page.groupName,
            description: page.groupDescription,
          });
        }
      }
    });
  }

  update(groupId: string): void {
    if (this.form.valid) {
      this.monitorService
        .groupUpdate(groupId, this.form.value)
        .subscribe(() => this.navService.go('/monitor'));
    }
  }
}
