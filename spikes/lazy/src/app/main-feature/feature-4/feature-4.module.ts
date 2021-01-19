import {ComponentFactory} from '@angular/core';
import {ComponentFactoryResolver} from '@angular/core';
import {NgModule} from '@angular/core';
import {Feature4aComponent} from './feature-4-a.component';
import {Feature4bComponent} from './feature-4-b.component';
import {Feature4cComponent} from './feature-4-c.component';
import {Feature4dComponent} from './feature-4-d.component';
import {Feature4Component} from './feature-4.component';

@NgModule({
  declarations: [
    Feature4Component,
    Feature4aComponent,
    Feature4bComponent,
    Feature4cComponent,
    Feature4dComponent,
  ]
})
export class Feature4Module {

  constructor(private componentFactoryResolver: ComponentFactoryResolver) {
  }

  public resolveComponent(): ComponentFactory<Feature4Component> {
    return this.componentFactoryResolver.resolveComponentFactory(Feature4Component);
  }

}
