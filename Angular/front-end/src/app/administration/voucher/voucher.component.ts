import { Component, OnInit } from '@angular/core';
import { Observable } from "rxjs";
import { Voucher } from "../../model/voucher";
import { UserService } from "../../service/user.service";

@Component({
  selector: 'app-voucher',
  templateUrl: './voucher.component.html',
  styleUrls: ['./voucher.component.scss']
})
export class VoucherComponent implements OnInit {
  vouchers$: Observable<Voucher[]>;

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.vouchers$ = this.userService.fetchAllVouchers();
  }

}
