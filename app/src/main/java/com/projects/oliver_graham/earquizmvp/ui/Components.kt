package com.projects.oliver_graham.earquizmvp.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
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

@Composable
fun LogoHeader() {

}

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
        if (clicked)
            Image(
                painterResource(id = R.drawable.open_eye),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(0.075f)
            )
        else
            Image(
                painterResource(id = R.drawable.closed_eye),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(0.075f)
            )
    }
}

@Composable
fun GenericTextField(
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    text: String,
    textStyle: TextStyle = LocalTextStyle.current,
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
        interactionSource = interactionSource
    )
}



@Composable
fun CircularIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    mutableEnabled: MutableState<Boolean> = mutableStateOf(true),
    buttonBackgroundColor: Color = Color.Unspecified,
    size: Dp = 50.dp,
    borderSize: Dp = 1.dp,
    borderColor: Color = MaterialTheme.colors.secondary,
    tint: Color = MaterialTheme.colors.primary
) {
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
        Icon(icon, contentDescription = "content description", tint = tint)
    }
}

@Composable
fun MediumButton(
    modifier: Modifier = Modifier,
    mutableEnabled: MutableState<Boolean> = mutableStateOf(true),
    onClick: () -> Unit,
    border: BorderStroke = BorderStroke(1.dp, MaterialTheme.colors.secondary),
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.elevation(4.dp, 16.dp),
    shape: Shape = RoundedCornerShape(25),
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

// TODO: Figure out button animation
//       create box that holds text and so replace every LargeButton with text
enum class ComponentState { Pressed, Released }

@Composable
fun LargeButton(
    //modifier: Modifier = Modifier,
    mutableEnabled: MutableState<Boolean> = mutableStateOf(true),
    onClick: () -> Unit,
    border: BorderStroke = BorderStroke(1.dp, MaterialTheme.colors.primaryVariant),
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.elevation(4.dp, 16.dp),
    shape: Shape = RoundedCornerShape(50),
    content: @Composable RowScope.() -> Unit
) {
/*    var toState by remember { mutableStateOf(ComponentState.Released) }
    val transition: Transition<ComponentState> = updateTransition(targetState = toState, label = "")
    // Defines a float animation to scale x,y
    val scaleX: Float by transition.animateFloat(
        transitionSpec = { spring(stiffness = 50f) }, label = ""
    ) { state ->
        if (state == ComponentState.Pressed) 0.925f else 1f
    }

    val scaleY: Float by transition.animateFloat(
        transitionSpec = { spring(stiffness = 50f) }, label = ""
    ) { state ->
        if (state == ComponentState.Pressed) 0.95f else 1f
    }

    val modifier = Modifier
        .size(width = (220 * scaleX).dp, height = (60 * scaleY).dp)
        .pointerInput(Unit) {
        detectTapGestures(
            onPress = {
                toState = ComponentState.Pressed
                tryAwaitRelease()
                toState = ComponentState.Released
            }
        )
    }*/

    // OLD MODIFIER
    //        modifier = Modifier
    //            .size(width = 220.dp, height = 60.dp)
    //            .alpha(0.825f),graphicsLayer(scaleX = scaleX, scaleY = scaleY)

   // Box(
    //    modifier = modifier
           // .size(width = (220 * scaleX).dp, height = (60 * scaleY).dp)
           // .alpha(0.825f)
  //  ) {

        Button(
            onClick = onClick,
            modifier = Modifier
                .size(width = 220.dp, height = 60.dp)
                .alpha(0.825f),//.graphicsLayer(scaleX = scaleX, scaleY = scaleY),
                //.size(width = (220 * scaleX).dp, height = (60 * scaleY).dp)
               // .alpha(0.825f)
                //.graphicsLayer(),
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
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null
) {
/*    var toState by remember { mutableStateOf(ComponentState.Released) }
    val transition: Transition<ComponentState> = updateTransition(targetState = toState, label = "")
    // Defines a float animation to scale x,y
    val scaleX: Float by transition.animateFloat(
        transitionSpec = { spring(stiffness = 50f) }, label = ""
    ) { state ->
        if (state == ComponentState.Pressed) 0.925f else 1f
    }

    val scaleY: Float by transition.animateFloat(
        transitionSpec = { spring(stiffness = 50f) }, label = ""
    ) { state ->
        if (state == ComponentState.Pressed) 0.95f else 1f
    }

    val modifier = Modifier
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    toState = ComponentState.Pressed
                    tryAwaitRelease()
                    toState = ComponentState.Released
                }
            )
        }*/
    Text(
        //modifier = modifier.graphicsLayer(scaleX = scaleX, scaleY = scaleY),
        text = text,
        fontFamily = fontFamily,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = color,
        textAlign = textAlign
    )
}

@Composable
fun TextWithLeadingIcon(
    text: String,
    icon: ImageVector,
    tint: Color = Color.Unspecified
) {
    Row {
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
    //radioID: String,
    radioID: Int,
    //radioChoice: MutableState<String>,      // reflect state change when clicking radio button
    radioChoice: MutableState<Int>,      // reflect state change when clicking radio button
    enableOtherUI: MutableState<Boolean>? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
        ) {

            RadioButton(
                selected = radioChoice.value == radioID,
                onClick = {
                    radioChoice.value = radioID
                    enableOtherUI?.value = true
                },
                modifier = Modifier.scale(1.75f, 1.75f)
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
    answerGroup: @Composable () -> Unit
) {
    // You can add background image to a Box just by placing it under all other views.
    // And matchParentSize modifier will stretch it to the parent size.
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painterResource(id = R.drawable.rollingsheetmusicbackground),
            contentDescription = "",
            contentScale = ContentScale.FillBounds, // or some other scale
            modifier = Modifier.matchParentSize()
        )
        answerGroup()
    }
}

/*@Composable
fun BackGroundImage() {
    // You can add background image to a Box just by placing it under all other views.
    // And matchParentSize modifier will stretch it to the parent size.
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "",
            contentScale = ContentScale.FillBounds, // or some other scale
            modifier = Modifier.matchParentSize()
        )
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            IconButton(
                onClick = { *//*...*//* },
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .fillMaxWidth()
            ) {
*//*                Icon(
                    Icons.Filled.Close,
                    contentDescription = "stringResource(id = R.string.close)",
                    modifier = Modifier.align(Alignment.CenterEnd)
                )*//*
            }
        }
    }
}*/
