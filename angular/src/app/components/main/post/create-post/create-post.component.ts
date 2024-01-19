import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { tap } from 'rxjs/operators';
import { catchError, throwError } from 'rxjs';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators
} from '@angular/forms';
import { CreatePostPayload } from './create-post.payload';
import { SubredditModel } from '../../subreddit/subreddit-response';
import { PostService } from '../../../../service/post.service';
import { SubredditService } from '../../../../service/subreddit.service';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.css'
})
export class CreatePostComponent implements OnInit {
  subreddits: Array<SubredditModel> = [];

  postPayload: CreatePostPayload;

  createPostForm: FormGroup;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private postService: PostService,
    private subredditService: SubredditService
  ) {
    this.postPayload = {
      postName: '',
      url: '',
      description: '',
      subredditName: ''
    };
    this.createPostForm = this.formBuilder.group({});
  }

  ngOnInit() {
    this.createPostForm = new FormGroup({
      postName: new FormControl('', Validators.required),
      subredditName: new FormControl('', Validators.required),
      url: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required)
    });

    this.subredditService.getAllSubreddits().pipe(
      catchError((error) => throwError(() => error))
    ).subscribe((data) => {
      this.subreddits = data;
    });
  }

  createPost() {
    const urlControl = this.createPostForm.get('url');
    const postNameControl = this.createPostForm.get('postName');
    const descriptionControl = this.createPostForm.get('description');
    const subredditNameControl = this.createPostForm.get('subredditName');
    if (postNameControl && subredditNameControl && urlControl && descriptionControl) {
      this.postPayload.subredditName = subredditNameControl.value;
      this.postPayload.description = descriptionControl.value;
      this.postPayload.postName = postNameControl.value;
      this.postPayload.url = urlControl.value;
    }

    this.postService.createPost(this.postPayload).pipe(
      tap(() => {
        this.discardPost();
      }),
      catchError((error) => throwError(() => error))
    );
  }

  discardPost() {
    this.router.navigateByUrl('/').then((navigationSuccess: boolean) => {
      if (navigationSuccess) {
        console.log('Navigation succeeded');
      } else {
        console.error('Navigation failed');
      }
    });
  }
}
