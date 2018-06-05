package com.billkainkoom.quickdialog

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import com.billkainkoom.quickdialog.QuickDialog
import com.billkainkoom.quickdialog.QuickDialogType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Quick Dialog"
        context = this

        button.setOnClickListener {
            d6(context!!)
        }


    }


    fun d1(context: Context) {
        QuickDialog(
                context = this,
                style = QuickDialogType.Message,
                title = "Hello World",
                message = "The quick dialog jumped over the old dialog",
                image = R.drawable.ic_info_outline_black_24dp)
                .overrideButtonNames("OK").overrideClicks({ ->
                    Toast.makeText(context, "Clicked on OK", Toast.LENGTH_SHORT).show()
                }).show()
    }

    fun d2(context: Context) {
        QuickDialog(
                context = context,
                style = QuickDialogType.Progress,
                title = "Please wait",
                message = "Walking round the world")
                .show()
    }

    fun d3(context: Context) {
        QuickDialog(
                context = context,
                style = QuickDialogType.Progress,
                title = "Please wait",
                message = "Walking round the world")
                .overrideButtonNames("Hide Progress")
                .overrideClicks({ ->
                    Toast.makeText(context, "Clicked on Hide Progress", Toast.LENGTH_SHORT).show()
                }).showPositiveButton()
                .show()
    }

    fun d4(context: Context) {
        QuickDialog(
                context = context,
                style = QuickDialogType.Alert,
                title = "Proceed",
                message = "Do you want to take this action")
                .overrideButtonNames("Yes", "No")
                .overrideClicks(positiveClick = { ->
                    Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show()
                }, negativeClick = { ->
                    Toast.makeText(context, "No", Toast.LENGTH_SHORT).show()
                })
                .show()
    }

    fun d5(context: Context) {
        QuickDialog(
                context = context,
                style = QuickDialogType.Alert,
                title = "Proceed",
                message = "Do you want to take this action")
                .overrideButtonNames("Yes", "No")
                .overrideClicks(positiveClick = { dismiss ->
                    if (true) {
                        Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                }, negativeClick = { dismiss ->
                    if (true) {
                        Toast.makeText(context, "No", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                })
                .show()
    }

    fun d6(context: Context) {
        QuickDialog(
                context = context,
                style = QuickDialogType.WithInput,
                title = "Verify Code",
                message = "Please verify the SMS code that was sent to you")
                .overrideButtonNames("Verify", "Cancel", "Re-send")
                .overrideClicks(positiveClick = { dismiss, inputText ->
                    if (inputText.length < 3) {
                        Toast.makeText(context, "Please enter a 4 digit code", Toast.LENGTH_SHORT).show()
                    } else if (inputText == "4000") {
                        Toast.makeText(context, "Verified", Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else {
                        Toast.makeText(context, "You entered the wrong code", Toast.LENGTH_SHORT).show()
                    }
                }, negativeClick = { dismiss, inputText ->
                    dismiss()
                }, neutralClick = { dismiss, inputText ->
                    //Your action
                    dismiss()
                })
                .withInputHint("Code")
                .withInputLength(4)
                .withInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
                .showNeutralButton()
                .show()
    }


}
