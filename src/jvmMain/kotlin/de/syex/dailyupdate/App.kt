package de.syex.dailyupdate

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@Preview
fun App() {
    val store = DailyUpdateStore()
    val goals = remember { store.goals }
    val meetings = remember { store.meetings }
    val clipboardManager = LocalClipboardManager.current

    var copiedToClipboardInfoVisible by remember { mutableStateOf(false) }

    MaterialTheme {
        Column {
            Updates(
                updates = goals,
                title = "Goals",
                onUpdateContentChange = store::onGoalContentChanged,
                onUpdateCompleteChange = store::onGoalCompleteChanged,
                onUpdateAdded = store::onGoalAdded,
                newUpdateButtonText = "New goal",
            )

            Updates(
                updates = meetings,
                title = "Meetings",
                onUpdateContentChange = store::onMeetingContentChanged,
                onUpdateCompleteChange = { _, _ -> },
                onUpdateAdded = store::onMeetingAdded,
                newUpdateButtonText = "New meeting",
            )

            Button(
                onClick = {
                    clipboardManager.setText(AnnotatedString(store.createUpdateText()))
                    copiedToClipboardInfoVisible = true
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Create daily update text")
            }

            if (copiedToClipboardInfoVisible) {
                rememberCoroutineScope().launch {
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

@Composable
fun Updates(
    updates: List<Update>,
    title: String,
    newUpdateButtonText: String,
    onUpdateContentChange: (Int, String) -> Unit,
    onUpdateCompleteChange: (Int, Boolean) -> Unit,
    onUpdateAdded: () -> Unit,
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
                    TextField(
                        value = update.content,
                        onValueChange = { onUpdateContentChange(index, it) },
                        modifier = Modifier.weight(1f)
                    )

//                    Checkbox(
//                        checked = goal.completed ?: false,
//                        onCheckedChange = { onGoalCompleteChange(index, it) }
//                    )
                }
            }
        }

        Button(
            onClick = onUpdateAdded,
            modifier = Modifier.padding(top = 16.dp).align(Alignment.End)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Text(modifier = Modifier.padding(start = 8.dp), text = newUpdateButtonText)
        }
    }
}