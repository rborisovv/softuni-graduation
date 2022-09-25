import {Component, OnDestroy, OnInit} from '@angular/core';
import {faFacebookF, faGithub, faGoogle, faTwitter} from "@fortawesome/free-brands-svg-icons";
import {faAddressCard, faEnvelope, faUser} from '@fortawesome/free-regular-svg-icons';
import {faKey, faLock} from '@fortawesome/free-solid-svg-icons';
import {faCalendarAlt} from "@fortawesome/free-regular-svg-icons/faCalendarAlt";
import {UserService} from "../../service/user.service";
import {Subscription} from "rxjs";
import {AbstractControl, FormControl, FormGroup, Validators} from "@angular/forms";
import {DatePipe} from "@angular/common";
import {environment} from "../../../environments/environment.prod";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = [];
  private apiHost = environment.apiHost;


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

  constructor(private userService: UserService,
              private datePipe: DatePipe, private router: Router) {
  }

  registerForm = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(30)]),
    email: new FormControl('', [Validators.required, Validators.email]),
    firstName: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]),
    lastName: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]),
    birthDate: new FormControl(undefined, [Validators.required]),
    password: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(50)]),
    confirmPassword: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(50)])
  })

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }

  public onRegister(): void {
    if (!this.username.value || !this.email.value || !this.firstName.value || !this.lastName.value
      || !this.birthDate.value || !this.password.value || !this.confirmPassword.value) {
      return;
    }
    const formData: FormData = this.userService.createFormData({
      'username': this.username.value, 'email': this.email.value,
      'firstName': this.firstName.value, 'lastName': this.lastName.value,
      'birthDate': this.datePipe.transform(this.birthDate.value, 'dd-MM-yyyy'),
      'password': this.password.value, 'confirmPassword': this.confirmPassword.value
    });

    const subscription: Subscription = this.userService.registerUser(formData).subscribe({
      next: () => {
        this.router.navigateByUrl('/login');
      }
    });

    this.subscriptions.push(subscription);
  }

  public onConfirmPasswordChange(): boolean {
    return this.password.value === this.confirmPassword.value;
  }

  get today() {
    let today = new Date();
    return this.datePipe.transform(today, 'yyyy-MM-dd');
  }

  get username(): AbstractControl {
    return this.registerForm.get('username');
  }

  get email(): AbstractControl {
    return this.registerForm.get('email');
  }

  get firstName(): AbstractControl {
    return this.registerForm.get('firstName');
  }

  get lastName(): AbstractControl {
    return this.registerForm.get('lastName');
  }

  get birthDate(): AbstractControl {
    return this.registerForm.get('birthDate');
  }

  get password(): AbstractControl {
    return this.registerForm.get('password');
  }

  get confirmPassword(): AbstractControl {
    return this.registerForm.get('confirmPassword');
  }
}
