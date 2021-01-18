import {OnInit} from '@angular/core';
import {Injector} from '@angular/core';
import {Compiler} from '@angular/core';
import {ViewContainerRef} from '@angular/core';
import {ViewChild} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'app-feature-4-container',
  template: `
    <div class="feature-container">
      Feature 4 container
      <ng-template #container></ng-template>
    </div>
  `
})
export class Feature4ContainerComponent implements OnInit {

  @ViewChild('container', {read: ViewContainerRef}) container: ViewContainerRef;

  constructor(private compiler: Compiler,
              private injector: Injector) {
  }

  ngOnInit(): void {
    this.loadFeature();
  }

  private loadFeature(): void {
    import('./feature-4/feature-4.module').then(({Feature4Module}) => {
      this.compiler.compileModuleAsync(Feature4Module).then(moduleFactory => {
        const moduleRef = moduleFactory.create(this.injector);
        const componentFactory = moduleRef.instance.resolveComponent();
        this.container.createComponent(componentFactory);
      });
    });
  }
}
