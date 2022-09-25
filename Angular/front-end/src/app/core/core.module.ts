import {NgModule} from '@angular/core';
import {HomeComponent} from './home/home.component';
import {CommonModule} from "../common/common.module";

@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule]
})
export class CoreModule {
}
