# VerificationCodeEditText [![](https://jitpack.io/v/FairyHeart/VerificationCodeEditText.svg)](https://jitpack.io/#FairyHeart/VerificationCodeEditText)

验证码或者密码输入框样式

<img src="https://github.com/FairyHeart/VerificationCodeEditText/blob/master/WechatIMG528.jpeg" width = "320" height = "560" alt="图片名称" 
align=center>

使用：<br />1、在布局文件中添加VerificationCodeEditText并设置相关自定义属性
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
***需要给控件设置宽高***<br />
<br />2、相关属性说明<br />


| **属性名** | **属性说明** |
| :---: | :---: |
| password | 设置是否是密码样式，true 则显示小圆点 |
| showCursor | 是否显示光标 |
| separateType | 按钮样式，@integer/type_hollow空心方形，type_solid=实心方形，type_underline=下划线 |
| maxLength | 设置显示框框个数，0表示根据屏幕自动计算 |
| corner | 设置圆角 dp |
| borderColor | 设置框框实心部分颜色，type_solid适用 |
| borderWidth | 设置边框粗细 dp |
| blockColor | 设置框框实心部分颜色，type_solid适用 |
| textColor | 设置文本绘制颜色 |
| blockSpacing | 设置框框间隙，type_solid、type_underline适用 |
| cursorDuration | 设置游标闪烁时长 毫秒 |
| cursorWidth | 设置进游标宽度 |
| cursorColor | 设置游标显示颜色 |


<br />3、java代码中设置监听器，也可以设置自定义的相关属性
```kotlin
        val lineEdit = findViewById<VerificationCodeEditText>(R.id.rect_edit)
        lineEdit.showCursor = true
        lineEdit.textChangedListener = object : VerificationCodeEditText.TextChangedListener {
            /**
             * 输入/删除监听
             *
             * @param changeText 输入/删除的字符
             */
            override fun textChanged(changeText: CharSequence?) {
            }

            /**
             * 输入完成
             * @param text 输入的字符
             */
            override fun textCompleted(text: CharSequence?) {
                Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
            }

        } 
```
