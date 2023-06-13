import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewRegistrationRequestsComponent } from './view-registration-requests.component';

describe('ViewRegistrationRequestsComponent', () => {
  let component: ViewRegistrationRequestsComponent;
  let fixture: ComponentFixture<ViewRegistrationRequestsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewRegistrationRequestsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewRegistrationRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
