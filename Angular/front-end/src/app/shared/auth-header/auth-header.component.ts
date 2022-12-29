import {Component, Input} from '@angular/core';
import {faCartShopping} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-auth-header',
  templateUrl: './auth-header.component.html',
  styleUrls: ['./auth-header.component.scss']
})
export class AuthHeaderComponent {
  @Input() url: string = '';

  faCart = faCartShopping;

}
