import { Pipe, PipeTransform } from '@angular/core';
import { map, Observable } from "rxjs";
import { Voucher } from "../model/voucher";
import { VoucherEnum } from "../enumeration/voucher-enum";

@Pipe({
  name: 'voucherTotal'
})
export class BasketDiscountedTotalPipe implements PipeTransform {

  transform(value: Observable<number>, voucher: Voucher): Observable<number> {
    return value.pipe(map((v) => {
      if (voucher.type === VoucherEnum.RELATIVE) {
        return v * (1 - voucher.discount / 100);
      }

      return v - voucher.discount;
    }));
  }
}
