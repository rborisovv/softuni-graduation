import {ChangeDetectionStrategy, Component} from '@angular/core';

@Component({
  selector: 'app-create-product',
  templateUrl: './create-product.component.html',
  styleUrls: ['./create-product.component.scss'],
  changeDetection:  ChangeDetectionStrategy.OnPush
})
export class CreateProductComponent {

}
