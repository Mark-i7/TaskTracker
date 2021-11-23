package com.example.trelloclone.ui.progressdialog

import android.app.Dialog

interface ProgressDialog {
    var mProgressDialog: Dialog

    fun showProgressDialog();
    fun hideProgressDialog();
}