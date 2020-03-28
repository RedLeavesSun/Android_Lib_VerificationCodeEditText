# VerificationCodeEditText 
[![](https://jitpack.io/v/FairyHeart/VerificationCodeEditText.svg)](https://jitpack.io/#FairyHeart/VerificationCodeEditText)

[中文使用](https://github.com/FairyHeart/VerificationCodeEditText/wiki/%E4%B8%AD%E6%96%87%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E)

Verification code or password input box style

<img src="https://github.com/FairyHeart/VerificationCodeEditText/blob/master/WechatIMG528.jpeg" width = "320" height = "560" alt="demo picture" 
align=center>

Use:<br />1. Add VerificationCodeEditText to the layout file and set relevant custom attributes
```xml
<com.lib.verification.code.VerificationCodeEditText
       android:id="@+id/rect_edit"
       android:layout_width="match_parent"
       android:layout_height="48dp"
       android:background="@null"
       android:inputType="number"
       app:layout_constraintLeft_toLeftOf="parent"
       app:layout_constraintRight_toRightOf="parent"
       app:layout_constraintTop_toTopOf="parent"
       app:blockColor="@color/lightGrey"
       app:borderColor="@color/colorPrimary"
       app:borderWidth="2dp"
       app:corner="10dp"
       app:cursorColor="#ff0000"
       app:cursorDuration="1000"
       app:cursorWidth="2dp"
       app:password="true"
       app:maxLength="4"
       app:separateType="@integer/type_hollow"
       app:showCursor="true"
       app:textColor="@color/colorAccent" />
```
*** Need to set width and height for the control ***<br />
<br />2. Description of related attributes<br />


| **Attribute name** | **Attribute description** |
| :---: | :---: |
| password | Set whether password style, true Display dots |
| showCursor | Whether to display the cursor |
| separateType | Button style, @ integer/type_hollow hollow square,type_solid = solid square,Type_equipments = underscores |
| maxLength | Set the number of display boxes. 0 indicates automatic calculation based on the screen. |
| corner | Set fillet dp |
| borderColor | Set the solid color of the box, type_solid is applicable |
| borderWidth | Set border thickness dp |
| blockColor | Set the solid color of the box, type_solid is applicable |
| textColor | Set the color of text drawing |
| blockSpacing | Set border gap, type_solid, type_underline applies |
| cursorDuration | Sets the cursor flicker duration in milliseconds |
| cursorWidth | Sets the width of the cursor |
| cursorColor | Sets the cursor display color |


<br />3. Set listeners in java code, and you can also set custom related attributes.
```kotlin
        val lineEdit = findViewById<VerificationCodeEditText>(R.id.rect_edit)
        lineEdit.showCursor = true
        lineEdit.textChangedListener = object : VerificationCodeEditText.TextChangedListener {
            /**
             * Enter / delete listener
             *
             * @param changeText Enter / delete characters
             */
            override fun textChanged(changeText: CharSequence?) {
            }

            /**
             * Input completed
             * @param text Entered characters
             */
            override fun textCompleted(text: CharSequence?) {
                Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
            }

        } 
```
