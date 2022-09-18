import { Component, OnInit } from '@angular/core';
import {faFacebookF, faGithub, faGoogle, faTwitter} from "@fortawesome/free-brands-svg-icons";
import { faUser } from '@fortawesome/free-regular-svg-icons';
import { faKey } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  faFacebook = faFacebookF;
  faTwitter = faTwitter;
  faGoogle = faGoogle;
  faGithub = faGithub;
  faUser = faUser;
  faKey = faKey;

  constructor() { }

  ngOnInit(): void {
  }

}
