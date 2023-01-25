import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {MediaService} from "../../service/media.service";
import {Observable} from "rxjs";
import {Media} from "../../interface/media";
import {faTrash} from '@fortawesome/free-solid-svg-icons';
import {Store} from "@ngrx/store";
import {deleteMediaAction} from "../../store/action/media.action";

@Component({
  selector: 'app-media',
  templateUrl: './media.component.html',
  styleUrls: ['./media.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class MediaComponent implements OnInit {
  constructor(private mediaService: MediaService, private readonly store: Store) {
  }

  faTrash = faTrash;
  medias$: Observable<Media[]>;

  ngOnInit(): void {
    this.medias$ = this.mediaService.fetchAllMedias();
  }

  public deleteCategory(pkOfFile: string) {
    this.store.dispatch(deleteMediaAction({pk: pkOfFile}));
  }
}
