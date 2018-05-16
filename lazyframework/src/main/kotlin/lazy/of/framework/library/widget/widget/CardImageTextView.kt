package lazy.of.framework.library.widget.widget

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.view_card_image_text.view.*
import lazy.of.framework.library.R


/**
 * @author piece.of.lazy
 */
class CardImageTextView : CardView {

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {

        val li = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as? LayoutInflater
        val view = li?.inflate(R.layout.view_card_image_text, this, false)

        if(view is LinearLayout) {
            containerView = view
            addView(containerView)

            with(containerView) {
                imageView = view_card_image_text_image
                textView = view_card_image_text_text
            }
            initAttrs(attrs, defStyleAttr)
        }
    }

    private var containerView: LinearLayout? = null
    private var imageView: ImageView? = null
    private var textView: TextView? = null

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.CardImageTextView)
        typedArray?.let {
            val orientation = it.getInt(R.styleable.CardImageTextView_orientation, 0)
            val background = it.getDrawable(R.styleable.CardImageTextView_backgroundCard)
            val paddingStart = it.getDimensionPixelSize(R.styleable.CardImageTextView_paddingStart, 0)
            val paddingEnd = it.getDimensionPixelSize(R.styleable.CardImageTextView_paddingEnd, 0)
            val paddingTop = it.getDimensionPixelSize(R.styleable.CardImageTextView_paddingTop, 0)
            val paddingBottom = it.getDimensionPixelSize(R.styleable.CardImageTextView_paddingBottom, 0)
            containerView?.apply {
                setOrientation(if(orientation == 1) LinearLayout.VERTICAL else LinearLayout.HORIZONTAL)
                setBackground(background)
                setPaddingRelative(paddingStart, paddingTop, paddingEnd, paddingBottom)
            }

            val imageWidth = it.getDimensionPixelSize(R.styleable.CardImageTextView_imageWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
            val imageHeight = it.getDimensionPixelSize(R.styleable.CardImageTextView_imageHeight, ViewGroup.LayoutParams.WRAP_CONTENT)
            val imageSpace = it.getDimensionPixelSize(R.styleable.CardImageTextView_paddingSpace, 0)
            val imageSrc = it.getDrawable(R.styleable.CardImageTextView_imageSrc)

            imageView?.apply {
                if(imageSrc != null && imageWidth != 0 && imageHeight != 0) {
                    visibility = View.VISIBLE
                    (layoutParams as? LinearLayout.LayoutParams)?.apply {
                        width = imageWidth
                        height = imageHeight
                        marginEnd = imageSpace
                    }
                    setImageDrawable(imageSrc)
                } else {
                    visibility = View.GONE
                }
            }

            val style = it.getInt(R.styleable.CardImageTextView_layoutStyle, 0)
            val textSize = it.getDimensionPixelSize(R.styleable.CardImageTextView_textSize, 0)
            val textColor = it.getColorStateList(R.styleable.CardImageTextView_textColor)
            val textStyle = it.getInt(R.styleable.CardImageTextView_textStyle, 0)
            val text = it.getText(R.styleable.CardImageTextView_text)

            textView?.apply {
                if(style == 1) {
                    (layoutParams as? LinearLayout.LayoutParams)?.apply {
                        width = 0
                        height = ViewGroup.LayoutParams.WRAP_CONTENT
                        weight = 1f
                    }
                } else {
                    (layoutParams as? LinearLayout.LayoutParams)?.apply {
                        width = ViewGroup.LayoutParams.WRAP_CONTENT
                        height = ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                }

                setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
                if(textColor != null) {
                    setTextColor(textColor)
                }
                typeface = if(textStyle == 1) {
                    Typeface.DEFAULT_BOLD
                } else {
                    Typeface.DEFAULT
                }
                setText(text)
            }
        }
        typedArray?.recycle()
    }
}