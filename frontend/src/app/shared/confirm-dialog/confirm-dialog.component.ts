import { Component, Inject }             from '@angular/core';
import { CommonModule }                    from '@angular/common';
import { MatButtonModule }                 from '@angular/material/button';
import { MatDialogModule,
         MatDialogRef,
         MAT_DIALOG_DATA }                 from '@angular/material/dialog';
import { MatIconModule }                   from '@angular/material/icon';

@Component({
  selector:   'app-confirm-dialog',
  standalone:  true,
  imports:    [CommonModule, MatButtonModule, MatDialogModule, MatIconModule],
  templateUrl: './confirm-dialog.component.html',
  styleUrl:    './confirm-dialog.component.css'
})
export class ConfirmDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {
      title:   string;
      message: string;
    }
  ) {}

  confirm(): void { this.dialogRef.close(true);  }
  cancel():  void { this.dialogRef.close(false); }
}
