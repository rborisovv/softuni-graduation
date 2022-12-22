import { Component, OnInit } from '@angular/core';
import {faAngleDown, faCartShopping, faSignOut} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-admin-header',
  templateUrl: './admin-header.component.html',
  styleUrls: ['./admin-header.component.scss']
})
export class AdminHeaderComponent implements OnInit {

  faAngleDown = faAngleDown;

  faCart = faCartShopping;

  faSignOut = faSignOut;

  constructor() { }

  ngOnInit(): void {
  }

}
