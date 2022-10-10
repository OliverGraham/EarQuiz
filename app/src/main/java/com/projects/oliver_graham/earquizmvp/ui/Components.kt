package com.projects.oliver_graham.earquizmvp.ui

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projects.oliver_graham.earquizmvp.R


// TODO: Figure out AnimateButtonPress()

@Composable
fun AnimateButtonPress() {
    val isPressed = remember { mutableStateOf(value = false) }
    val transition: Transition<Boolean> = updateTransition(targetState = isPressed.value, label = "")
    // Defines a float animation to scale x,y
    val scaleX: Float by transition.animateFloat(
        transitionSpec = { spring(stiffness = 50f) }, label = ""
    ) { state ->
        if (state == isPressed.value) 0.90f else 1f
    }

    val scaleY: Float by transition.animateFloat(
        transitionSpec = { spring(stiffness = 50f) }, label = ""
    ) { state ->
        if (state == isPressed.value) 0.90f else 1f
    }

    val modifier = Modifier.pointerInput(Unit) {
        detectTapGestures(
            onPress = {
                isPressed.value = true
                tryAwaitRelease()
                isPressed.value = false
            }
        )
    }
}

@Composable
fun EyeBallIcon(
    onClick: () -> Unit,
    clicked: Boolean
) {
    // clickable icon
    Box(modifier = Modifier.clickable(onClick = { onClick() })
    ) { ->
        val darkTheme = isSystemInDarkTheme()
        val icon = if (clicked) {
            if (darkTheme) R.drawable.open_eye_dark_theme else R.drawable.open_eye
        } else {
            if (darkTheme) R.drawable.closed_eye_dark_theme else R.drawable.closed_eye
        }

        EyeBall(icon = icon)
    }
}

@Composable
private fun EyeBall(icon: Int) {
    Image(
        painterResource(id = icon),
        contentDescription = "",
        modifier = Modifier.fillMaxSize(fraction = 0.075f)
    )
}

@Composable
fun GenericTextField(
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    text: String,
    textStyle: TextStyle = LocalTextStyle.current.copy(MaterialTheme.colors.secondaryVariant),
    label: String,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    onTextChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        value = text,
        onValueChange = onTextChange,
        textStyle = textStyle,
        label = { Text(label) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        interactionSource = interactionSource,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedLabelColor = MaterialTheme.colors.secondaryVariant
        )
    )
}



@Composable
fun CircularIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    mutableEnabled: MutableState<Boolean> = mutableStateOf(value = true),
    size: Dp = 50.dp,
    borderSize: Dp = 1.dp
) {

    val buttonBackgroundColor: Color
    val iconColor: Color
    val borderColor: Color
    if (isSystemInDarkTheme()) {
        if (mutableEnabled.value) {
            // enabled button dark color
            buttonBackgroundColor = MaterialTheme.colors.background
            iconColor = MaterialTheme.colors.secondary
            borderColor = MaterialTheme.colors.secondary
        } else {
            // disabled dark color
            buttonBackgroundColor = MaterialTheme.colors.background
            iconColor = MaterialTheme.colors.primary
            borderColor = MaterialTheme.colors.primary
        }
    } else {
        if (mutableEnabled.value) {
            // enabled light color
            buttonBackgroundColor = MaterialTheme.colors.primary
            iconColor = MaterialTheme.colors.secondary
            borderColor = MaterialTheme.colors.secondary
        } else {
            // disabled light color
            buttonBackgroundColor = MaterialTheme.colors.background
            iconColor = MaterialTheme.colors.primary
            borderColor = MaterialTheme.colors.primary
        }
    }

    IconButton(
        onClick = onClick,
        enabled = mutableEnabled.value,
        modifier = Modifier
            .then(Modifier.size(size))
            .border(
                width = borderSize,
                color = borderColor,
                shape = CircleShape
            )
            .background(color = buttonBackgroundColor, shape = CircleShape)

    ) {
        Icon(icon, contentDescription = "content description", tint = iconColor)
    }
}

@Composable
fun MediumButton(
    modifier: Modifier = Modifier,
    mutableEnabled: MutableState<Boolean> = mutableStateOf(value = true),
    onClick: () -> Unit,
    border: BorderStroke = BorderStroke(1.dp, MaterialTheme.colors.secondary),
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.elevation(4.dp, 16.dp),
    shape: Shape = RoundedCornerShape(percent = 25),
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(width = 180.dp, height = 45.dp),
        elevation = elevation,
        enabled = mutableEnabled.value,
        border = border,
        shape = shape,
        colors = colors,
        content = content,
    )
}

@Composable
fun LargeButton(
    //modifier: Modifier = Modifier,
    mutableEnabled: MutableState<Boolean> = mutableStateOf(value = true),
    onClick: () -> Unit,
    border: BorderStroke = BorderStroke(1.dp, MaterialTheme.colors.primaryVariant),
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.elevation(4.dp, 16.dp),
    shape: Shape = RoundedCornerShape(percent = 50),
    content: @Composable RowScope.() -> Unit
) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .size(width = 220.dp, height = 60.dp)
                .alpha(0.825f),
            elevation = elevation,
            enabled = mutableEnabled.value,
            border = border,
            shape = shape,
            colors = colors,
            content = content,
        )
    //}
}

@Composable
fun LargeText(
    text: String,
    fontFamily: FontFamily? = null,
    fontSize: TextUnit = 22.sp,
    fontWeight: FontWeight? = null,
    color: Color = if (isSystemInDarkTheme()) MaterialTheme.colors.onPrimary else Color.Unspecified,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        fontFamily = fontFamily,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = color,
        textAlign = textAlign
    )
}

@Composable
fun ThemeButton(
    text: String,
    onButtonClick: () -> Unit,
    mutableEnabled: MutableState<Boolean>,
    innerContent: (@Composable () -> Unit)? = null
) {
    val darkTheme = isSystemInDarkTheme()
    LargeButton(
        onClick = { onButtonClick() },
        mutableEnabled = mutableEnabled,
        border = getButtonBorder(
            enabled = mutableEnabled.value,
            darkTheme = darkTheme
        ),
    ) { ->

        ButtonText(
            text = text,
            enabled = mutableEnabled.value,
            darkTheme = darkTheme
        )
        if (innerContent != null) innerContent()
    }
}

@Composable
private fun getButtonBorder(
    enabled: Boolean,
    darkTheme: Boolean
): BorderStroke {
    return if (darkTheme) {
        val color = if (enabled) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.background
        BorderStroke(1.dp, color)
    } else {
        val color = if (enabled) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primary
        BorderStroke(1.dp, color)
    }
}

@Composable
private fun ButtonText(
    text: String,
    enabled: Boolean,
    darkTheme: Boolean
)  {
    LargeText(
        text = text,
        color = if (darkTheme) {
            if (enabled) MaterialTheme.colors.onPrimary else MaterialTheme.colors.background
        } else {
            if (enabled) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primary
        }
    )
}

@Composable
fun TextWithLeadingIcon(
    text: String,
    icon: ImageVector,
    tint: Color = Color.Unspecified
) {
    Row { ->
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = tint
        )
        Spacer(Modifier.padding(horizontal = 4.dp))
        LargeText(text = text)
    }
}

@Composable
fun TextAndRadio(
    text: String,
    radioID: Int,
    radioChoice: MutableState<Int>,
    enableOtherUI: MutableState<Boolean>? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) { ->
        Column(
            modifier = Modifier.padding(start = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) { ->
            LargeText(
                text = text,
                fontSize = 24.sp
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            horizontalAlignment = Alignment.End
        ) { ->
            RadioButton(
                selected = radioChoice.value == radioID,
                onClick = {
                    radioChoice.value = radioID
                    enableOtherUI?.value = true
                },
                modifier = Modifier.scale(scaleX = 1.75f, scaleY = 1.75f)
            )
        }
    }
}

@Composable
fun CenteredContentRow(
    modifier: Modifier = Modifier,
    padding: Dp = 16.dp,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalArrangement: Arrangement.HorizontalOrVertical = Arrangement.SpaceEvenly,
    rowContent: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(padding),
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement,
        content = rowContent
    )
}


@Composable
fun BackGroundImage(
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) { ->
        Image(
            painterResource(id = R.drawable.rollingsheetmusicbackground),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
        content()
    }
}

