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
import { PaymentSuccessfulComponent } from './payment-successful/payment-successful.component';
import { CheckoutGuard } from "../guard/checkout.guard";
import { OrderFlowGuard } from "../guard/order.flow.guard";
import { CheckoutHeaderComponent } from './checkout-header/checkout-header.component';

const routes: Routes = [
  {
    path: '', canActivate: [PageGuard], children: [
      { path: 'cart', component: CartComponent },
      { path: 'checkout', component: CheckoutComponent, canActivate: [CheckoutGuard] },
      { path: 'finalize-order', component: FinalizeComponent, canActivate: [OrderFlowGuard] },
      { path: 'order-created', component: PaymentSuccessfulComponent }
    ]
  }
];

@NgModule({
  declarations: [
    CartComponent,
    CartBalancePipe,
    CheckoutComponent,
    FinalizeComponent,
    PaymentSuccessfulComponent,
    CheckoutHeaderComponent
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
