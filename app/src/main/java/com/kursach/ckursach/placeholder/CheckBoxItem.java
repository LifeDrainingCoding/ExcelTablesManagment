package com.kursach.ckursach.placeholder;

public class CheckBoxItem {
    private String fileName;
    private boolean checked;
    public CheckBoxItem(String fileName ) {
     this.fileName = fileName;
     this.checked = false;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
