import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MaprRequestsDetailComponent } from './mapr-requests-detail.component';

describe('MaprRequests Management Detail Component', () => {
  let comp: MaprRequestsDetailComponent;
  let fixture: ComponentFixture<MaprRequestsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MaprRequestsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ maprRequests: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MaprRequestsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MaprRequestsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load maprRequests on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.maprRequests).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
