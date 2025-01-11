import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskListDisplayComponent } from './task-list-display.component';

describe('TaskListDisplayComponent', () => {
  let component: TaskListDisplayComponent;
  let fixture: ComponentFixture<TaskListDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TaskListDisplayComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TaskListDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
