import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Validators } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { MonitorGroupPage } from '@api/common/monitor';
import { Util } from '@app/components/shared';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';

@Injectable()
export class MonitorGroupUpdatePageService {
  private readonly _groupDescription = this.nav.state('description');
  private readonly _apiResponse = Util.response<MonitorGroupPage>();

  readonly groupName = this.nav.param('groupName');
  readonly apiResponse = this._apiResponse.asReadonly();

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

  constructor(
    private nav: NavService,
    private monitorService: MonitorService
  ) {}

  init(): void {
    this.monitorService.group(this.groupName()).subscribe((response) => {
      this._apiResponse.set(response);
      if (response.result) {
        this._groupDescription.set(response.result.groupDescription);
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
        .subscribe(() => this.nav.go('/monitor'));
    }
  }
}
