import { Pipe, PipeTransform } from '@angular/core';
import { DatePipe } from "@angular/common";

@Pipe({
  name: 'dateFormat'
})
export class DateFormatPipe implements PipeTransform {

  transform(value: Date | string, format: string = 'dd-MM-yyyy'): string {
    const datePipe = new DatePipe('en-US');
    const formattedDate = datePipe.transform(value, 'yyyy-MM-dd');
    return datePipe.transform(formattedDate, format);
  }
}
