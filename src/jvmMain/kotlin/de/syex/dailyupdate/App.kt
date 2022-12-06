package de.syex.dailyupdate

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@Preview
fun App() {
    val store = DailyUpdateStore()
    val goals = remember { store.goals }
    val clipboardManager = LocalClipboardManager.current

    MaterialTheme {
        Column {
            Goals(
                goals = goals,
                onGoalContentChange = store::onGoalContentChanged,
                onGoalCompleteChange = store::onGoalCompleteChanged,
                onGoalAdded = store::onGoalAdded
            )

            Button(
                onClick = {
                    clipboardManager.setText(AnnotatedString(store.createUpdateText()))
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Create daily update text")
            }
        }

    }
}

@Composable
fun Goals(
    goals: List<Goal>,
    onGoalContentChange: (Int, String) -> Unit,
    onGoalCompleteChange: (Int, Boolean) -> Unit,
    onGoalAdded: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
    ) {

        Text(
            modifier = Modifier.padding(vertical = 16.dp),
            style = TextStyle(fontSize = 28.sp),
            text = "Goals"
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(goals) { index, goal ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = goal.content,
                        onValueChange = { onGoalContentChange(index, it) },
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
            onClick = onGoalAdded,
            modifier = Modifier.padding(top = 16.dp).align(Alignment.End)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Text(modifier = Modifier.padding(start = 8.dp), text = "New goal")
        }
    }
}