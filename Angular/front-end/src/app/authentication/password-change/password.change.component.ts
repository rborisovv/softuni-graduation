import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { PasswordReset } from "../../interface/passwordReset";
import { ActivatedRoute, ActivatedRouteSnapshot } from "@angular/router";
import { Store } from "@ngrx/store";
import { changePassword } from "../../store/action/user.action";
import { pipe, take } from "rxjs";

@Component({
  selector: 'app-password-change',
  templateUrl: './password.change.component.html',
  styleUrls: ['./password.change.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PasswordChangeComponent implements OnInit {
  token: string | undefined;

  constructor(private activatedRoute: ActivatedRoute, private store: Store) {
  }

  passwordReset = new FormGroup({
    password: new FormControl('', [Validators.required,
      Validators.minLength(6), Validators.maxLength(50)]),
    confirmPassword: new FormControl('', [Validators.required,
      Validators.minLength(6), Validators.maxLength(50)])
  });

  ngOnInit(): void {
    let allShowHidePassword = document.querySelectorAll('.password-showHide');
    allShowHidePassword.forEach(item => {
      item.addEventListener('click', () => {
        item.classList.toggle('hide')
        if (item.closest('.form-input').querySelector('input').type === 'password') {
          item.closest('.form-input').querySelector('input').type = 'text'
        } else {
          item.closest('.form-input').querySelector('input').type = 'password'
        }
      })
    });

    this.activatedRoute.queryParams.pipe(
      take(1),
    ).subscribe(params => {
      this.token = params['token'];
    })
  }

  onConfirmPasswordChange() {
    return this.password.value === this.confirmPassword.value;
  }

  get password() {
    return this.passwordReset.get('password');
  }

  get confirmPassword() {
    return this.passwordReset.get('confirmPassword');
  }

  //TODO: Add a directive here and on register for password confirmation check
  onSubmit() {

    const data: PasswordReset = {
      password: this.password.value,
      confirmPassword: this.confirmPassword.value,
      token: this.token
    };

    this.store.dispatch(changePassword({ data }));
  }
}
