import { NgModule } from '@angular/core';
import { EditorModule } from '@tinymce/tinymce-angular';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutes } from './app.routes';
import { HeaderComponent } from './components/header/header.component';
import { SignupComponent } from './components/auth/signup/signup.component';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/auth/login/login.component';
import { HomeComponent } from './components/main/home/home.component';
import {
  SubredditSideBarComponent
} from './components/main/subreddit/subreddit-side-bar/subreddit-side-bar.component';
import {
  PostTileComponent
} from './components/main/post/post-tile/post-tile.component';
import {
  VoteButtonComponent
} from './components/main/vote/vote-button/vote-button.component';
import {
  SideBarComponent
} from './components/side-bar/side-bar.component';
import {
  CreatePostComponent
} from './components/main/post/create-post/create-post.component';
import { TokenInterceptor } from './service/token.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    HeaderComponent,
    SignupComponent,
    SideBarComponent,
    PostTileComponent,
    VoteButtonComponent,
    CreatePostComponent,
    SubredditSideBarComponent
  ],
  imports: [
    AppRoutes,
    RouterModule,
    EditorModule,
    BrowserModule,
    HttpClientModule,
    FontAwesomeModule,
    ReactiveFormsModule,
    ToastrModule.forRoot(),
    BrowserAnimationsModule,
    NgxWebstorageModule.forRoot()
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: TokenInterceptor,
    multi: true
  }],
  bootstrap: [AppComponent]
})
export class AppModule {
}
