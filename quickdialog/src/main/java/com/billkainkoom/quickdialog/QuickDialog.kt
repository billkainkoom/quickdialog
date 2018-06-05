package com.billkainkoom.quickdialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.text.Html
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*

/**
 * The quick dialog is a simple dialog helper that helps you create consistent variations of dialogs for your app
 */
class QuickDialog(
        context: Context,
        style: QuickDialogType,
        title: String = "",
        message: String = "",
        image: Int = R.drawable.ic_info_outline_black_24dp,
        val optionList: Array<String> = Array(0, { i -> "" })
) {

    /**
     * Tags and references to views on xml
     */
    private val TAG = "QuickDialog"
    private var quickDialog: Dialog? = null
    private val titleView: TextView get() = quickDialog?.findViewById(R.id.title)!!
    private val messageView: TextView get() = quickDialog?.findViewById(R.id.info)!!
    private val inputEditText: EditText get() = quickDialog?.findViewById(R.id.input)!!
    private val imageView: ImageView get() = quickDialog?.findViewById(R.id.image)!!
    private val positiveButton: Button get() = quickDialog?.findViewById(R.id.positive_button)!!
    private val negativeButton: Button get() = quickDialog?.findViewById(R.id.negative_button)!!
    private val neutralButton: Button get() = quickDialog?.findViewById(R.id.neutral_button)!!
    private val progressBar: ProgressBar get() = quickDialog?.findViewById(R.id.progress_bar)!!
    private val spinnerContainer: LinearLayout get() = quickDialog?.findViewById(R.id.spinner_container)!!


    val spinner: Spinner get() = quickDialog?.findViewById(R.id.spinner)!!
    val inputText: String get() = inputEditText.text.toString()
    var optionSelectedPosition = 0
    var optionSelectedValue = ""


    private fun setDialogImage(image: Int) {
        imageView.setImageResource(image)
    }

    private fun setDialogImage(image: BitmapDrawable) {
        imageView.setImageBitmap(image.bitmap)
    }


    private fun setDialogTitle(messsage: String) {
        titleView.text = messsage
    }

    private fun setDialogMessage(infoMessage: String?) {
        messageView.text = Html.fromHtml(infoMessage)
    }

    init {
        initialize(context, style)
        setDialogTitle(title)
        setDialogMessage(message)
        setDialogImage(image)
    }

    fun hideButtons(): QuickDialog {
        positiveButton.visibility = View.GONE
        negativeButton.visibility = View.GONE
        neutralButton.visibility = View.GONE
        return this
    }

    fun overrideClicks(
            positiveClick: () -> Unit = {},
            negativeClick: () -> Unit = {},
            neutralClick: () -> Unit = {}
    ): QuickDialog {
        positiveButton.setOnClickListener {
            positiveClick()
            dismiss()
        }
        negativeButton.setOnClickListener {
            negativeClick()
            dismiss()
        }
        neutralButton.setOnClickListener {
            neutralClick()
            dismiss()
        }
        return this
    }

    fun overrideClicks(
            positiveClick: (dismiss: () -> Unit) -> Unit = { d -> },
            negativeClick: (dismiss: () -> Unit) -> Unit = { d -> },
            neutralClick: (dismiss: () -> Unit) -> Unit = { d -> }
    ): QuickDialog {
        positiveButton.setOnClickListener {
            positiveClick(dismiss)
        }
        negativeButton.setOnClickListener {
            negativeClick(dismiss)
        }
        neutralButton.setOnClickListener {
            neutralClick(dismiss)
        }
        return this
    }

    fun overrideClicks(
            positiveClick: (dismiss: () -> Unit, inputText: String) -> Unit = { d, s -> },
            negativeClick: (dismiss: () -> Unit, inputText: String) -> Unit = { d, s -> },
            neutralClick: (dismiss: () -> Unit, inputText: String) -> Unit = { d, s -> }
    ): QuickDialog {
        positiveButton.setOnClickListener {
            positiveClick(dismiss, inputText)
        }
        negativeButton.setOnClickListener {
            negativeClick(dismiss, inputText)
        }
        neutralButton.setOnClickListener {
            neutralClick(dismiss, inputText)
        }
        return this
    }

    fun withInputHint(hint: String): QuickDialog {
        inputEditText.hint = hint
        return this
    }

    fun withInputLength(length: Int): QuickDialog {
        inputEditText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(length))
        return this
    }

    fun withInputType(inputType: Int): QuickDialog {
        try {
            inputEditText.inputType = inputType
        } catch (e: Exception) {
            Log.e(TAG, "Invalid input type")
            e.printStackTrace()
        }
        return this
    }

    fun showPositiveButton(): QuickDialog {
        positiveButton.visibility = View.VISIBLE
        return this
    }

    fun showNeutralButton(): QuickDialog {
        neutralButton.visibility = View.VISIBLE
        return this
    }

    fun hideNeutralButton(): QuickDialog {
        neutralButton.visibility = View.GONE
        return this
    }

    fun overrideButtonNames(positive: String = "OK", negative: String = "Cancel", neutral: String = ""): QuickDialog {
        positiveButton.text = positive.toUpperCase()
        negativeButton.text = negative.toUpperCase()
        neutralButton.text = neutral.toUpperCase()
        return this
    }

    private fun initialize(context: Context, style: QuickDialogType) {
        quickDialog = Dialog(context)
        quickDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        quickDialog?.setContentView(R.layout.quick_dialog)

        quickDialog?.setCancelable(false)


        when (style) {
            QuickDialogType.Progress -> {
                imageView.visibility = View.GONE
                negativeButton.visibility = View.GONE
                positiveButton.visibility = View.GONE
            }
            QuickDialogType.Alert -> {
                progressBar.visibility = View.GONE
                spinnerContainer.visibility = View.GONE
            }
            QuickDialogType.Message -> {
                progressBar.visibility = View.GONE
                negativeButton.visibility = View.GONE
                spinnerContainer.visibility = View.GONE
            }
            QuickDialogType.WithInput -> {
                progressBar.visibility = View.GONE
                inputEditText.visibility = View.VISIBLE
                neutralButton.visibility = View.GONE
                spinnerContainer.visibility = View.GONE
            }
            QuickDialogType.Option -> {
                progressBar.visibility = View.GONE
                spinnerContainer.visibility = View.VISIBLE

                spinner.adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, optionList)
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(adapterView: AdapterView<*>, view: View, index: Int, l: Long) {
                        optionSelectedPosition = index
                        optionSelectedValue = optionList[index]
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>) {
                    }
                }
            }
        }

        /**
         * Set default click behaviour on buttons
         */
        overrideClicks(positiveClick = { ->

        }, negativeClick = { ->

        }, neutralClick = { ->

        })
    }

    fun show(): QuickDialog {
        try {
            quickDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return this
    }

    val dismiss: () -> Unit = {
        try {
            quickDialog?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

