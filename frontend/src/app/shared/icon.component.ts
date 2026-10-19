import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-icon',
  standalone: true,
  styles: [`
  :host {
    display: inline-flex;
    width: 16px;
    height: 16px;
    min-width: 16px;
    flex: 0 0 16px;
    align-items: center;
    justify-content: center;
    line-height: 0;
  }
  svg {
    display: block;
    width: 100%;
    height: 100%;
    max-width: 100%;
    max-height: 100%;
    overflow: visible;
  }
  `],
  template: `
  <svg class="ui-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
  @switch(name){
    @case('dashboard'){<rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/>}
    @case('users'){<path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M22 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75"/>}
    @case('user'){<path d="M20 21a8 8 0 0 0-16 0"/><circle cx="12" cy="7" r="4"/>}
    @case('card'){<rect x="2" y="5" width="20" height="14" rx="2"/><path d="M2 10h20M6 15h4"/>}
    @case('bank'){<path d="m3 10 9-6 9 6M5 10h14M6 10v7m4-7v7m4-7v7m4-7v7M3 20h18"/>}
    @case('list'){<path d="M8 6h13M8 12h13M8 18h13M3 6h.01M3 12h.01M3 18h.01"/>}
    @case('shield'){<path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10"/><path d="m9 12 2 2 4-4"/>}
    @case('bell'){<path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9M10 21h4"/>}
    @case('logout'){<path d="M10 17l5-5-5-5M15 12H3M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4"/>}
    @case('search'){<circle cx="11" cy="11" r="7"/><path d="m20 20-4-4"/>}
    @case('filter'){<path d="M4 5h16l-6 7v5l-4 2v-7z"/>}
    @case('edit'){<path d="M12 20h9M16.5 3.5a2.1 2.1 0 0 1 3 3L8 18l-4 1 1-4z"/>}
    @case('ban'){<circle cx="12" cy="12" r="9"/><path d="m6 6 12 12"/>}
    @case('lock'){<rect x="5" y="10" width="14" height="11" rx="2.5"/><path d="M8.5 10V7.5a3.5 3.5 0 0 1 7 0V10"/><path d="M12 14.5v2.5"/>}
    @case('location'){<path d="M20 10c0 5-8 11-8 11S4 15 4 10a8 8 0 1 1 16 0"/><circle cx="12" cy="10" r="2.5"/>}
    @case('transfer'){<path d="m7 7 4-4M7 7l4 4M7 7h13M17 17l-4 4M17 17l-4-4M17 17H4"/>}
    @case('trend'){<path d="M3 17 9 11l4 4 8-8"/><path d="M14 7h7v7"/>}
    @case('chevron'){<path d="m9 18 6-6-6-6"/>}
    @case('plus'){<path d="M12 5v14M5 12h14"/>}
    @case('clock'){<circle cx="12" cy="12" r="9"/><path d="M12 7v5l3 2"/>}
    @case('bolt'){<path d="m13 2-9 12h8l-1 8 9-12h-8z"/>}
    @case('download'){<path d="M12 3v12M7 10l5 5 5-5M5 21h14"/>}
    @case('upload'){<path d="M12 21V9M7 14l5-5 5 5M5 3h14"/>}
  }
  </svg>`
})
export class IconComponent { @Input() name = ''; }
