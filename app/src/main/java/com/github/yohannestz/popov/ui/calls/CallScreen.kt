package com.github.yohannestz.popov.ui.calls

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.yohannestz.popov.ui.viewmodels.CallsViewModel

@Composable
fun CallScreen (callsViewModel: CallsViewModel = hiltViewModel()) {
    val state = callsViewModel.state.value

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        if (state.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Calculating...",
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6
                )
            }

        } else if (state.callLogList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No data...",
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6
                )
            }
        } else {

            val list = remember {
                state.callLogList
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    count = list.size,
                    key = {
                        it
                    },
                    itemContent = {index ->
                        val listItem = remember {
                            list[index]
                        }
                        Text(
                            text = listItem.toString(),
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                        )
                    }
                )
            }
        }
    }
}