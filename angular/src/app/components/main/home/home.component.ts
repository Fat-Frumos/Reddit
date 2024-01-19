import { Component } from '@angular/core';
import { PostService } from '../../../service/post.service';
import { PostModel } from '../post/post.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  posts: Array<PostModel> = [];

  constructor(private postService: PostService) {
    this.postService.getAllPosts().subscribe((post) => {
      this.posts = post;
    });
  }
}
