import {Routes} from '@angular/router';

export const routes: Routes = [
  /*{
    path: '',
    component: LoginComponent
  },
  {
    path: 'add-candidate',
    component: FormCandidateComponent
  },
  {
    path: 'edit-candidate',
    component: FormCandidateComponent
  },
  {
    path: 'skill',
    component: CrudSkillComponent
  },*/
  {
    path: '**',
    redirectTo: ''
  }
];
