import { Component, Inject }          from '@angular/core';
import { CommonModule }                from '@angular/common';
import { MatButtonModule }             from '@angular/material/button';
import { MatDialogModule,
         MatDialogRef,
         MAT_DIALOG_DATA }             from '@angular/material/dialog';
import { MatSelectModule }             from '@angular/material/select';
import { MatFormFieldModule }          from '@angular/material/form-field';
import { FormsModule }                 from '@angular/forms';
import { MatIconModule }               from '@angular/material/icon';

@Component({
  selector:    'app-assign-dialog',
  standalone:   true,
  imports:     [CommonModule, MatButtonModule, MatDialogModule,
                MatSelectModule, MatFormFieldModule, FormsModule, MatIconModule],
  templateUrl: './assign-dialog.component.html',
  styleUrl:    './assign-dialog.component.css'
})
export class AssignDialogComponent {
  selectedId: number | null = null;

  constructor(
    public dialogRef: MatDialogRef<AssignDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {
      title:        string;
      items:        any[];
      displayField: string;
    }
  ) {}

  confirm(): void {
    if (this.selectedId) this.dialogRef.close(this.selectedId);
  }

  cancel(): void { this.dialogRef.close(null); }
}
