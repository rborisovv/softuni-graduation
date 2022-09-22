import { Component, OnInit } from '@angular/core';
import {faFacebookF, faGithub, faGoogle, faTwitter} from "@fortawesome/free-brands-svg-icons";
import {faAddressCard, faEnvelope, faUser } from '@fortawesome/free-regular-svg-icons';
import {faKey, faLock } from '@fortawesome/free-solid-svg-icons';
import {faCalendarAlt} from "@fortawesome/free-regular-svg-icons/faCalendarAlt";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  faFacebook = faFacebookF;
  faTwitter = faTwitter;
  faGoogle = faGoogle;
  faGithub = faGithub;
  faUser = faUser;
  faKey = faKey;
  faLock = faLock;
  faAddressCard = faAddressCard;
  faEnvelope = faEnvelope;
  faCalendar = faCalendarAlt;

  constructor() { }

  ngOnInit(): void {
  }

}
