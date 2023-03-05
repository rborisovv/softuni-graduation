import {Component, OnInit} from '@angular/core';
import {faFacebook, faGoogle, faLinkedin} from '@fortawesome/free-brands-svg-icons';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  faGoogle = faGoogle;
  faFacebook = faFacebook;
  faLinkedIn = faLinkedin;

  constructor() {
  }

  ngOnInit(): void {
  }

}
