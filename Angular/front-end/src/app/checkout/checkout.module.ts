import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartComponent } from './cart/cart.component';
import { RouterModule, Routes } from "@angular/router";
import { PageGuard } from "../guard/page.guard";
import { SharedModule } from "../shared/shared.module";
import { CartBalancePipe } from "../pipes/cart.balance.pipe";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { CheckoutComponent } from './checkout/checkout.component';
import { ReactiveFormsModule } from "@angular/forms";
import { FinalizeComponent } from './finalize/finalize.component';

const routes: Routes = [
  {
    path: '', canActivate: [PageGuard], children: [
      { path: 'cart', component: CartComponent },
      { path: 'checkout', component: CheckoutComponent },
      { path: 'finalize-order', component: FinalizeComponent }
    ]
  }
];

@NgModule({
  declarations: [
    CartComponent,
    CartBalancePipe,
    CheckoutComponent,
    FinalizeComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    SharedModule,
    FontAwesomeModule,
    ReactiveFormsModule
  ]
})
export class CheckoutModule {
}
