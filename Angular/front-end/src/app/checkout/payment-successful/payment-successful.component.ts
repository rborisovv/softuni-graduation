import { Component } from '@angular/core';
import { Router } from "@angular/router";

@Component({
  selector: 'app-payment-successful',
  templateUrl: './payment-successful.component.html',
  styleUrls: ['./payment-successful.component.scss']
})
export class PaymentSuccessfulComponent {
  constructor(protected readonly router: Router) {
  }
}