import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import bohnanza.viewmodel.GameViewModel

@Composable
fun StartScreen(viewModel: GameViewModel) {
    var playerCount by remember { mutableStateOf(2) }
    var names by remember { mutableStateOf(List(2) {""}) }
    var flagPlayer by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (!flagPlayer) {
                Button(onClick = { if (playerCount > 2) playerCount-- }) { Text("-") }
                Button(onClick = { if (playerCount < 7) playerCount++ }) { Text("+") }
                Text("Количество игроков: ${playerCount}")
                Button(onClick = {
                    names = List(playerCount) { "" }
                    flagPlayer = true
                }) { Text("Далее") }
            } else {
                Text("Bohnanza")
                names.forEachIndexed { i, name ->
                    TextField(
                        value = name,
                        label = { Text("Игрок ${i + 1}") },
                        onValueChange = { newValue ->
                            names = names.toMutableList().also { it[i] = newValue }
                        }
                    )
                }
                Button(onClick = { viewModel.startGame(names) }) {
                    Text("Начать игру")
                }
            }
        }
    }
}