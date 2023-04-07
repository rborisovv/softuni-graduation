export function mouseClickListener(item): void {
  item.classList.toggle('hide')
  if (item.closest('.form-input').querySelector('input').type === 'password') {
    item.closest('.form-input').querySelector('input').type = 'text';
  } else {
    item.closest('.form-input').querySelector('input').type = 'password';
  }
}

export function passwordShowHideOption(): void {
  const allShowHidePassword = document.querySelectorAll('.password-showHide');

  allShowHidePassword.forEach(item => {
    item.addEventListener('click', () => {
      mouseClickListener(item);
    });
  });
}

export function removeListenersOnDestroy(): void {
  const allShowHidePassword = document.querySelectorAll('.password-showHide');

  allShowHidePassword.forEach(x => {
    x.removeEventListener("click", mouseClickListener);
  });
}

export function checkPasswordEquality(password: string, confirmPassword: string): boolean {
  return password == confirmPassword;
}
