import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CartComponent} from './cart/cart.component';
import {RouterModule, Routes} from "@angular/router";
import {PageGuard} from "../guard/page.guard";
import {SharedModule} from "../shared/shared.module";
import {CartBalancePipe} from "../pipes/cart.balance.pipe";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";

const routes: Routes = [
  {
    path: '', canActivate: [PageGuard], children: [
      {path: 'cart', component: CartComponent}
    ]
  }
];

@NgModule({
  declarations: [
    CartComponent,
    CartBalancePipe
  ],
    imports: [
        CommonModule,
        RouterModule.forChild(routes),
        SharedModule,
        FontAwesomeModule
    ]
})
export class CheckoutModule {
}
