import {ChangeDetectionStrategy, Component, OnDestroy, OnInit} from '@angular/core';
import {MediaService} from "../../service/media.service";
import {NotifierService} from "angular-notifier";
import {catchError, Observable, Subscription} from "rxjs";
import {Media} from "../../interface/media";
import {faTrash} from '@fortawesome/free-solid-svg-icons';
import {Router} from "@angular/router";
import {NotificationType} from "../../enumeration/notification-enum";

@Component({
  selector: 'app-media',
  templateUrl: './media.component.html',
  styleUrls: ['./media.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class MediaComponent implements OnInit, OnDestroy {
  constructor(private mediaService: MediaService, private notifier: NotifierService,
              private router: Router) {
  }

  private subscriptions: Subscription[] = [];

  faTrash = faTrash;

  medias$: Observable<Media[]>;

  ngOnInit(): void {
    this.medias$ = this.mediaService.fetchAllMedias();
  }

  public deleteCategory(pkOfFile: string) {
    const subscription = this.mediaService.deleteMedia(pkOfFile)
      .pipe(
        catchError((err) => {
          this.notifier.notify(NotificationType.ERROR, err.error.message);
          throw err;
        })
      )
      .subscribe({
        next: (response) => {
          this.router.navigateByUrl('/admin/cockpit')
            .then(() => {
              this.notifier.notify(NotificationType.SUCCESS, response.message);
            });
        }
      });

    this.subscriptions.push(subscription);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(x => x.unsubscribe());
  }
}
