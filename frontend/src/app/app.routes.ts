import { Routes } from '@angular/router';
import { authGuard } from './core/guard/auth.guard';

export const routes: Routes = [
    {
        path: 'auth/login',
        loadComponent: () => import('./features/auth/login/login').then(m => m.LoginComponent)
    },
    {
        path: 'loans/list',
        loadComponent: () => import('./features/loans/loan-list/loan-list').then(m => m.LoanListComponent),
        canActivate: [authGuard],
        data: { role: 'ROLE_USER' }
    },
    {
        path: 'loans/request',
        loadComponent: () => import('./features/loans/loan-request/loan-request').then(m => m.LoanRequestComponent),
        canActivate: [authGuard],
        data: { role: 'ROLE_USER' }
    },
    {
        path: 'dashboard/admin',
        loadComponent: () => import('./features/dashboard/admin/admin').then(m => m.AdminComponent),
        canActivate: [authGuard],
        data: { role: 'ROLE_ADMIN' }
    },
    {
        path: '',
        redirectTo: 'auth/login',
        pathMatch: 'full'
    },
    {
        path: 'auth/register',
        loadComponent: () => import('./features/auth/register/register').then(m => m.RegisterComponent)
    },

    {
        path: '**',
        redirectTo: 'auth/login'
    }
];
