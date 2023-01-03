import {Component, ElementRef, ViewChild} from '@angular/core';
import {CategoryService} from "../../service/category.service";
import {Category} from "../../interface/category";
import {Router} from "@angular/router";
import {NotifierService} from "angular-notifier";
import {NotificationType} from "../../enumeration/notification-enum";
import {createFormData} from "../../service/service.index";

@Component({
  selector: 'app-create-category',
  templateUrl: './create-category.component.html',
  styleUrls: ['./create-category.component.scss']
})
export class CreateCategoryComponent {
  constructor(private categoryService: CategoryService, private router: Router, private notifier: NotifierService) {
  }

  @ViewChild('media') mediaInput: ElementRef;

  mediaPath: string;
  imageSrc: string | ArrayBuffer;


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
      reader.onload = () => this.imageSrc = reader.result;

      reader.readAsDataURL(file);
    }
  }

  createCategory(category: Category): void {
    category.media = this.mediaInput.nativeElement.files[0];

    this.categoryService.createCategory(createFormData(category))
      .subscribe({
        next: (response) => {
          this.router.navigateByUrl('/admin/cockpit').then(() => {
            this.notifier.notify(NotificationType.SUCCESS, `${response.message}`);
          });
        }
      });
  }
}
