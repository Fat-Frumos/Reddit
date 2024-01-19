import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PostModel } from '../components/main/post/post.model';
import {
  CreatePostPayload
} from '../components/main/post/create-post/create-post.payload';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  baseUrl = 'https://key-cloak.onrender.com/api/';

  http = inject(HttpClient);

  getAllPosts(): Observable<Array<PostModel>> {
    return this.http.get<Array<PostModel>>(`${this.baseUrl}post`);
  }

  createPost(postPayload: CreatePostPayload): Observable<PostModel> {
    return this.http.post<PostModel>(`${this.baseUrl}post`, postPayload);
  }

  getPost(id: number): Observable<PostModel> {
    return this.http.get<PostModel>(`${this.baseUrl}post/${id}`);
  }

  getAllPostsByUser(name: string): Observable<PostModel[]> {
    return this.http.get<PostModel[]>(`${this.baseUrl}post/user/${name}`);
  }
}
