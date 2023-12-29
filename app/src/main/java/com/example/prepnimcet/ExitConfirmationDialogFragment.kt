package com.example.prepnimcet

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ExitConfirmationDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                // Handle exit action here (e.g., finish the activity)
                activity?.finishAffinity()
            }
            .setNegativeButton("No") { _: DialogInterface, _: Int ->
                // Dismiss the dialog, no action needed
            }
            .create()
    }
}