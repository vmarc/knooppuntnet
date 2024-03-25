import { computed } from '@angular/core';
import { Injectable } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { LocationNode } from '@api/common/location';
import { AnalysisStrategy } from '@app/core';
import { PreferencesService } from '@app/core';
import { RouterService } from '../../../shared/services/router.service';
import { LocationService } from '../location.service';
import { LocalLocationNode } from './components/local-location-node';
import { LocationModeService } from './components/location-mode.service';
import { LocationSelectionService } from './location-selection.service';

@Injectable()
export class LocationSelectionPageService {
  private readonly routerService = inject(RouterService);
  private readonly locationService = inject(LocationService);
  private readonly locationSelectionService = inject(LocationSelectionService);
  private readonly locationModeService = inject(LocationModeService);
  private readonly router = inject(Router);
  private readonly preferencesService = inject(PreferencesService);

  private readonly _locationNode = signal<LocalLocationNode | null>(null);
  readonly locationNode = this._locationNode.asReadonly();

  readonly key = this.locationService.key;
  readonly networkType = computed(() => this.locationService.key().networkType);
  readonly country = computed(() => this.locationService.key().country);
  readonly isModeName = this.locationModeService.isModeName;
  readonly isModeTree = this.locationModeService.isModeTree;

  onInit() {
    this.preferencesService.setStrategy(AnalysisStrategy.location);
    this.locationService.initPage(this.routerService);
    this.locationSelectionService
      .locations(this.networkType(), this.country())
      .subscribe((response) => {
        this._locationNode.set(this.toLocalLocationNode([], response));
      });
  }

  locationSelected(locationName: string): void {
    const url = `/analysis/${this.networkType()}/${this.country()}/${locationName}/nodes`;
    this.router.navigateByUrl(url);
  }

  private toLocalLocationNode(
    parents: LocationNode[],
    locationNode: LocationNode
  ): LocalLocationNode {
    const localPath = parents.map((ln) => ln.name).join(':');
    const childParents: LocationNode[] = [];
    parents.forEach((parent) => childParents.push(parent));
    childParents.push(locationNode);

    let localChildren: LocalLocationNode[] = [];
    if (locationNode.children) {
      localChildren = locationNode.children.map((child) =>
        this.toLocalLocationNode(childParents, child)
      );
    }

    return {
      path: localPath,
      name: locationNode.name,
      nodeCount: locationNode.nodeCount,
      children: localChildren,
    };
  }
}
