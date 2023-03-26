import { Component, OnInit } from '@angular/core';
import { OrderService } from "../../service/order.service";
import { Observable } from "rxjs";
import { Orders } from "../../interface/orders";

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit {
  orders$: Observable<Orders[]>;

  constructor(private orderService: OrderService) {
  }

  ngOnInit(): void {
    this.orders$ = this.orderService.fetchAllOrders();
  }
}

//TODO: Make a delete order functionality
