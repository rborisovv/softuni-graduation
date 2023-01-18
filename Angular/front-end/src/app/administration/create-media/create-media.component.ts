import {ChangeDetectorRef, Component, ElementRef, ViewChild} from '@angular/core';
import {map, Observable} from "rxjs";
import {AbstractControl, AsyncValidatorFn, FormControl, FormGroup, ValidationErrors, Validators} from "@angular/forms";
import {MediaService} from "../../service/media.service";
import {createFormData} from "../../service/service.index";
import {NotificationType} from "../../enumeration/notification-enum";
import {Media} from "../../interface/media";
import {Router} from "@angular/router";
import {NotifierService} from "angular-notifier";

@Component({
  selector: 'app-create-media',
  templateUrl: './create-media.component.html',
  styleUrls: ['./create-media.component.scss']
})
export class CreateMediaComponent {
  constructor(private changeDetectorRef: ChangeDetectorRef, private mediaService: MediaService,
              private router: Router, private notifier: NotifierService) {
  }

  @ViewChild('mediaInput') mediaInput: ElementRef;
  @ViewChild('mediaSearchInput') mediaSearchInput: ElementRef;
  @ViewChild('categorySelectionElement') categorySelectionElement: ElementRef;
  @ViewChild('productSelectionElement') productSelectionElement: ElementRef;

  mediaPath: string;
  imageSrc: string | ArrayBuffer;

  createMediaGroup = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(4),
      Validators.maxLength(40)], [nameValidator.validateName(this.mediaService)]),
    media: new FormControl('', Validators.required)
  });


  public handleMediaUploadClick(): void {
    this.mediaInput.nativeElement.click();
  }

  public onMediaUpload(event: any) {
    this.mediaPath = this.mediaInput.nativeElement.value;
    this.readURL(event);
  }

  readURL(event: any): void {
    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];

      const reader = new FileReader();
      reader.onload = () => {
        this.imageSrc = reader.result;
        this.changeDetectorRef.markForCheck();
      };

      reader.readAsDataURL(file);
    }
  }

  get name() {
    return this.createMediaGroup.get('name');
  }

  get media() {
    return this.createMediaGroup.get('media');
  }

  createMedia() {
    const mediaData: Media = {
      name: this.name.value.trim(),
      file: this.mediaInput.nativeElement.files[0]
    }

    this.mediaService.createMedia(createFormData(mediaData))
      .subscribe({
        next: (response) => {
          this.router.navigateByUrl('/admin/cockpit').then(() => {
            this.notifier.notify(NotificationType.SUCCESS, `${response.message}`);
          });
        }
      });
  }
}

class nameValidator {
  static validateName(mediaService: MediaService): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors> => {
      return mediaService.isMediaByNamePresent(control.value)
        .pipe(map((result: boolean) => result ? {namePresent: true} : null));
    }
  }
}
