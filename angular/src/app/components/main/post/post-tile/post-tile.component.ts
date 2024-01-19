import { Component, Input, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { faComments } from '@fortawesome/free-solid-svg-icons';
import { PostModel } from '../post.model';

@Component({
  selector: 'app-post-tile',
  templateUrl: './post-tile.component.html',
  styleUrls: ['./post-tile.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class PostTileComponent {
  faComments = faComments;

  @Input() posts: PostModel[];

  constructor(private router: Router) {
    this.posts = [];
  }

  goToPost(id: number): void {
    this.router.navigateByUrl(`/view-post/${id}`).then((success: boolean) => {
      if (success) {
        console.log('Navigation succeeded');
      } else {
        console.error('Navigation failed');
      }
    });
  }
}
