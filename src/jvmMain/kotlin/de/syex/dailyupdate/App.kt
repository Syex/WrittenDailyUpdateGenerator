package de.syex.dailyupdate

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App() {
    val store = DailyUpdateStore()
    val goals = remember { store.goals }
    val meetings = remember { store.meetings }
    val clipboardManager = LocalClipboardManager.current

    var copiedToClipboardInfoVisible by remember { mutableStateOf(false) }

    MaterialTheme {
        Column(
            modifier = Modifier.onKeyEvent {
                when {
                    it.isMetaPressed && it.key == Key.G && it.type == KeyEventType.KeyUp -> {
                        store.onGoalAdded()
                        true
                    }

                    it.isMetaPressed && it.key == Key.M && it.type == KeyEventType.KeyUp -> {
                        store.onMeetingAdded()
                        true
                    }

                    else -> false
                }
            }
        ) {
            Updates(
                updates = goals,
                title = "Goals",
                onUpdateContentChange = store::onGoalContentChanged,
                onUpdateAdded = store::onGoalAdded,
                newUpdateButtonText = "New goal",
                shortcutKey = "G"
            )

            Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 2.dp)

            Updates(
                updates = meetings,
                title = "Meetings",
                onUpdateContentChange = store::onMeetingContentChanged,
                onUpdateAdded = store::onMeetingAdded,
                newUpdateButtonText = "New meeting",
                shortcutKey = "M"
            )

            Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 2.dp)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(store.createUpdateText()))
                        copiedToClipboardInfoVisible = true
                    },
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Text("Create daily update text")
                }

                Button(
                    onClick = { store.onCleared() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White)
                ) {
                    Text("Clear")
                }
            }

            if (copiedToClipboardInfoVisible) {
                LaunchedEffect(Unit) {
                    delay(2000)
                    copiedToClipboardInfoVisible = false
                }
                Text(
                    text = "✅ Copied to clipboard",
                    style = TextStyle(color = Color.Green),
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 4.dp)
                )
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Updates(
    updates: List<Update>,
    title: String,
    newUpdateButtonText: String,
    onUpdateContentChange: (Int, String) -> Unit,
    onUpdateAdded: () -> Unit,
    shortcutKey: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
    ) {

        Text(
            modifier = Modifier.padding(vertical = 16.dp),
            style = TextStyle(fontSize = 28.sp),
            text = title
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(updates) { index, update ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    val focusRequester = remember { FocusRequester() }
                    TextField(
                        value = update.content,
                        onValueChange = { onUpdateContentChange(index, it) },
                        modifier = Modifier.weight(1f).focusRequester(focusRequester)
                    )

                    LaunchedEffect(Unit) {
                        if (update.content.isEmpty()) focusRequester.requestFocus()
                    }
                }
            }
        }

        TooltipArea(
            tooltip = {
                Surface(
                    modifier = Modifier.shadow(4.dp),
                    color = Color(255, 255, 210),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "⌘ + $shortcutKey",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        ) {
            Button(
                onClick = onUpdateAdded,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Text(modifier = Modifier.padding(start = 8.dp), text = newUpdateButtonText)
            }
        }
    }
}