import { Routes } from '@angular/router';
import { LoginComponent } from './Componentes/login/login.component';
import { HomeComponent } from './Componentes/home/home.component';
import { RegisterComponent } from './Componentes/register/register.component';
import { ActividadesComponent } from './Componentes/actividades/actividades.component';
import { PerfilComponent } from './Componentes/perfil/perfil.component';
import { PerfilFormComponent } from './Componentes/perfil-form/perfil-form.component';
import { ActividadFormComponent } from './Componentes/actividad-form/actividad-form.component';
import { MiPerfilComponent } from './Componentes/mi-perfil/mi-perfil.component';

export const routes: Routes = [
    { path: 'register', component: RegisterComponent },
    { path: 'login', component: LoginComponent },
    { path: 'home', component: HomeComponent },
    { path: 'actividades', component: ActividadesComponent },
    { path: 'actividad/edit/:idOfertante/:idActividad', component: ActividadFormComponent },
    { path: 'miPerfil', component: MiPerfilComponent },
    { path: 'perfil/:usuario', component: PerfilComponent },
    { path: 'perfil/edit/:idOfertante/:idConsumidor', component: PerfilFormComponent },
    { path: '', redirectTo: '/login', pathMatch: 'full' }
];
