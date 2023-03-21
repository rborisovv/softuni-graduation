import { Component, OnDestroy, OnInit, Renderer2 } from '@angular/core';
import { faBurger, faCartShopping } from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-hamburger',
  templateUrl: './hamburger.component.html',
  styleUrls: ['./hamburger.component.scss']
})
export class HamburgerComponent implements OnInit, OnDestroy {
  burger = faBurger;
  body: HTMLBodyElement = document.querySelector('body');
  cart = faCartShopping;

  constructor(private renderer: Renderer2) {

  }

  ngOnDestroy(): void {
    this.renderer.setStyle(this.body, 'overflowY', 'auto');
  }

  ngOnInit(): void {

  }


  onHamburgerClick(event: MouseEvent) {
    const headerElement = document.querySelector("header");
    // @ts-ignore
    if (event.target.checked) {
      this.renderer.setStyle(this.body, 'overflowY', 'hidden');
      this.renderer.setStyle(headerElement, 'overflowY', 'hidden');
      this.renderer.setStyle(headerElement, 'background', '#ffffff');
    } else {
      this.renderer.setStyle(this.body, 'overflowY', 'auto');
      this.renderer.setStyle(headerElement, 'background', 'transparent');
    }
  }
}
